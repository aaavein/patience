package net.aaavein.patience.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.aaavein.patience.api.CraftingContainer;
import net.aaavein.patience.client.sound.CraftingSoundInstance;
import net.aaavein.patience.client.sound.SoundRegistry;
import net.aaavein.patience.config.ConfigManager;
import net.aaavein.patience.config.ContainerSettings;
import net.aaavein.patience.config.PatienceConfig;
import net.aaavein.patience.network.CraftingExhaustionPayload;
import net.aaavein.patience.util.SlotRange;
import net.aaavein.patience.util.SpeedCalculator;

import java.util.ArrayList;
import java.util.List;

public final class CraftingHandler {
    private static final Logger LOGGER = LogManager.getLogger(CraftingHandler.class);
    private static final float BASE_CRAFT_TIME = 20.0F;
    private static final double MOVEMENT_THRESHOLD = 0.01;
    private static final double VELOCITY_THRESHOLD = 0.01;
    private static final int SOUND_PITCH_INTERVAL = 25;
    private static final ContainerSettings DISABLED = ContainerSettings.builder().enabled(false).build();

    private static CraftingHandler instance;

    private PatienceConfig config;
    private AbstractContainerScreen<?> currentScreen;
    private ContainerSettings currentContainer;

    private boolean crafting;
    private boolean continuous;
    private float currentTime;
    private float totalTime;
    private int waitTicks;

    private double startX;
    private double startY;
    private double startZ;

    private CraftingSoundInstance currentSound;
    private String currentSoundId;
    private int soundTicks;

    private CraftingHandler() {}

    public static void initialize() {
        instance = new CraftingHandler();
    }

    public static CraftingHandler getInstance() {
        if (instance == null) {
            initialize();
        }
        return instance;
    }

    public void loadConfig() {
        config = ConfigManager.load();
    }

    public PatienceConfig getConfig() {
        return config;
    }

    public void setConfig(PatienceConfig config) {
        this.config = config;
    }

    public boolean isCrafting() {
        return crafting;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setScreen(Object screen) {
        this.currentScreen = (AbstractContainerScreen<?>) screen;
    }

    public void clearScreen() {
        this.currentScreen = null;
        stopCrafting();
    }

    public ContainerSettings getCurrentContainerSettings() {
        if (currentScreen == null || config == null) {
            return DISABLED;
        }

        String screenClass = currentScreen.getClass().getName();
        return config.getContainers().stream()
                .filter(c -> c.getScreenClass() != null && c.getScreenClass().equals(screenClass))
                .findFirst()
                .orElse(DISABLED);
    }

    public boolean handleSlotClick(Object screen, int slotId, boolean shiftHeld) {
        setScreen(screen);
        ContainerSettings container = getCurrentContainerSettings();

        if (config != null && config.isDebug()) {
            LOGGER.info("Slot click: slot={}, screen={}, shift={}",
                    slotId, currentScreen.getClass().getName(), shiftHeld);
        }

        if (isCreative()) {
            return false;
        }

        if (!container.isEnabled()) {
            return false;
        }

        if (slotId != container.getOutputSlot()) {
            stopCrafting();
            return false;
        }

        if (isSlotEmpty(container.getOutputSlot())) {
            return true;
        }

        if (isPlayerMoving()) {
            logDebug("Player moving, blocking craft");
            return true;
        }

        if (!canAffordCraft()) {
            logDebug("Cannot afford craft");
            return true;
        }

        if (!crafting) {
            startCrafting(container, shiftHeld);
        }

        return true;
    }

    public boolean shouldBlockSlotClick(Object screen, int slotId) {
        if (config == null) {
            return false;
        }

        if (isCreative()) {
            return false;
        }

        setScreen(screen);
        ContainerSettings container = getCurrentContainerSettings();
        return container.isEnabled() && slotId == container.getOutputSlot();
    }

    private void startCrafting(ContainerSettings container, boolean continuous) {
        this.currentContainer = container;
        this.continuous = continuous;
        this.crafting = true;

        float newTotalTime = calculateCraftTime(container);
        if (!config.isDecayEnabled() || currentTime > newTotalTime) {
            this.currentTime = 0;
        }

        this.totalTime = newTotalTime;

        recordPosition();

        if (totalTime >= 10.0F && config.isSoundsEnabled()) {
            playCraftingSound(getEffectiveCraftingSound(container));
        }
    }

    private void stopCrafting() {
        this.crafting = false;
        this.continuous = false;
        this.currentContainer = null;

        if (!config.isDecayEnabled()) {
            this.currentTime = 0;
        }

        stopSound();
    }

    public void tick() {
        if (!hasPlayer() || config == null) {
            return;
        }

        if (isCreative()) {
            if (crafting) stopCrafting();
            return;
        }

        ContainerSettings container = getCurrentContainerSettings();

        if (!container.isEnabled()) {
            if (config.isDecayEnabled() && currentTime > 0) {
                decayProgress();
            } else {
                currentTime = 0;
            }
            return;
        }

        if (crafting) {
            tickSound();
            tickCrafting(container);
        } else if (config.isDecayEnabled() && currentTime > 0) {
            decayProgress();
        }
    }

    private void decayProgress() {
        currentTime -= config.getDecayRate();
        if (currentTime <= 0) {
            currentTime = 0;
            stopCrafting();
        }
    }

    private void tickCrafting(ContainerSettings container) {
        if (config.isDecayEnabled()) {
            if (isPlayerMoving()) {
                decayProgress();
                stopSound();
                recordPosition();
                return;
            } else {
                if (currentSound == null && config.isSoundsEnabled() && totalTime >= 10.0F) {
                    playCraftingSound(getEffectiveCraftingSound(container));
                }
                recordPosition();
            }
        } else {
            if (hasPlayerMoved()) {
                logDebug("Player moved, stopping");
                stopCrafting();
                return;
            }
        }

        if (!canAffordCraft()) {
            logDebug("Can no longer afford, stopping");
            stopCrafting();
            return;
        }

        int outputSlot = container.getOutputSlot();

        if (isSlotEmpty(outputSlot)) {
            if (++waitTicks > 5) {
                waitTicks = 0;
                stopCrafting();
            }
            return;
        }

        if (shouldStopForCarried(outputSlot)) {
            stopCrafting();
            return;
        }

        if (currentTime < totalTime) {
            float speed = SpeedCalculator.getCraftingSpeed(
                    getPlayerLevel(),
                    config.getBaseCraftingSpeed(),
                    config.getSpeedPerLevel(),
                    config.getMaxLevelCap()
            );
            currentTime += speed * config.getExperienceMultiplier();
        } else {
            completeCraft(container);
        }
    }

    private void completeCraft(ContainerSettings container) {
        if (config.getExhaustionCost() > 0) {
            PacketDistributor.sendToServer(new CraftingExhaustionPayload(config.getExhaustionCost()));
        }

        if (config.isSoundsEnabled()) {
            playFinishSound(getEffectiveFinishSound(container));
        }

        SlotRange slots = container.getIngredientSlots();
        Object oldItems = getSlotItems(slots);

        ((CraftingContainer) currentScreen).patience$completeCraft(
                currentScreen.getMenu().getSlot(container.getOutputSlot()),
                container.getOutputSlot()
        );

        if (continuous) {
            Object newItems = getSlotItems(slots);
            if (!oldItems.equals(newItems) || !canAffordCraft()) {
                stopCrafting();
            } else {
                waitTicks = 0;
                currentTime = 0;
                if (totalTime >= 10.0F && config.isSoundsEnabled()) {
                    playCraftingSound(getEffectiveCraftingSound(container));
                }
            }
        } else {
            stopCrafting();
        }
    }

    private float calculateCraftTime(ContainerSettings container) {
        float globalMult = config.getGlobalTimeMultiplier();
        float containerMult = container.getTimeMultiplier();

        float ingredientTime = 0.0F;
        for (int slot : container.getIngredientSlots()) {
            if (isSlotEmpty(slot)) continue;

            ItemInfo info = getSlotItemInfo(slot);
            if (info == null) continue;

            float modMult = config.getIngredientMultipliers().getByMod()
                    .getOrDefault(info.modId, 1.0F);
            float itemMult = config.getIngredientMultipliers().getByItem()
                    .getOrDefault(info.id, 1.0F);
            ingredientTime += modMult * itemMult;
        }

        ItemInfo outputInfo = getSlotItemInfo(container.getOutputSlot());
        float outputMult = 1.0F;
        if (outputInfo != null) {
            float modMult = config.getOutputMultipliers().getByMod()
                    .getOrDefault(outputInfo.modId, 1.0F);
            float itemMult = config.getOutputMultipliers().getByItem()
                    .getOrDefault(outputInfo.id, 1.0F);
            outputMult = modMult * itemMult;
        }

        return BASE_CRAFT_TIME * ingredientTime * outputMult * containerMult * globalMult;
    }

    private String getEffectiveCraftingSound(ContainerSettings container) {
        String sound = container.getCraftingSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultCraftingSound();
    }

    private String getEffectiveFinishSound(ContainerSettings container) {
        String sound = container.getFinishSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultFinishSound();
    }

    @OnlyIn(Dist.CLIENT)
    private void playCraftingSound(String soundId) {
        stopSound();
        currentSoundId = soundId;
        soundTicks = 0;
        playSound(soundId);
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(String soundId) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (soundId != null && !soundId.isEmpty()) {
                currentSound = new CraftingSoundInstance(ResourceLocation.parse(soundId));
            } else {
                currentSound = new CraftingSoundInstance();
            }
            Minecraft.getInstance().getSoundManager().play(currentSound);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void stopSound() {
        if (currentSound != null) {
            currentSound.forceStop();
            currentSound = null;
        }
        soundTicks = 0;
    }

    @OnlyIn(Dist.CLIENT)
    private void tickSound() {
        if (currentSound != null && crafting) {
            if (++soundTicks >= SOUND_PITCH_INTERVAL) {
                soundTicks = 0;
                if (!currentSound.isForceStopped()) {
                    currentSound.forceStop();
                }
                playSound(currentSoundId);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playFinishSound(String soundId) {
        stopSound();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            SoundEvent sound = (soundId != null && !soundId.isEmpty())
                    ? SoundEvent.createVariableRangeEvent(ResourceLocation.parse(soundId))
                    : SoundRegistry.FINISH.get();
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(sound, CraftingSoundInstance.randomizePitch(), 0.1F)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean hasPlayer() {
        return Minecraft.getInstance().player != null;
    }

    @OnlyIn(Dist.CLIENT)
    private int getPlayerLevel() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null ? player.experienceLevel : 0;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isCreative() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getAbilities().instabuild;
    }

    @OnlyIn(Dist.CLIENT)
    private void recordPosition() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            startX = player.getX();
            startY = player.getY();
            startZ = player.getZ();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean hasPlayerMoved() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        double dx = Math.abs(player.getX() - startX);
        double dy = Math.abs(player.getY() - startY);
        double dz = Math.abs(player.getZ() - startZ);

        return dx > MOVEMENT_THRESHOLD || dy > MOVEMENT_THRESHOLD || dz > MOVEMENT_THRESHOLD;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isPlayerMoving() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        Vec3 velocity = player.getDeltaMovement();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        return speed > VELOCITY_THRESHOLD;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean canAffordCraft() {
        if (currentScreen == null) return true;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return true;

        if (currentScreen instanceof AnvilScreen) {
            AnvilMenu menu = (AnvilMenu) currentScreen.getMenu();
            int cost = menu.getCost();

            if (cost <= 0) return false;
            if (!player.getAbilities().instabuild && player.experienceLevel < cost) {
                return false;
            }
        }

        return true;
    }

    private boolean isSlotEmpty(int slot) {
        if (currentScreen == null) return true;
        return currentScreen.getMenu().getSlot(slot).getItem().isEmpty();
    }

    private boolean shouldStopForCarried(int outputSlot) {
        if (currentScreen == null) return true;

        ItemStack output = currentScreen.getMenu().getSlot(outputSlot).getItem();
        ItemStack carried = currentScreen.getMenu().getCarried();

        return !carried.isEmpty() &&
                (!ItemStack.isSameItem(output, carried) ||
                        output.getCount() + carried.getCount() > carried.getMaxStackSize());
    }

    private Object getSlotItems(SlotRange range) {
        List<Item> items = new ArrayList<>();
        if (currentScreen == null) return items;

        for (int slot : range) {
            items.add(currentScreen.getMenu().getSlot(slot).getItem().getItem());
        }
        return items;
    }

    private ItemInfo getSlotItemInfo(int slot) {
        if (currentScreen == null) return null;

        Item item = currentScreen.getMenu().getSlot(slot).getItem().getItem();
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(item);
        return new ItemInfo(loc.toString(), loc.getNamespace());
    }

    private void logDebug(String message) {
        if (config != null && config.isDebug()) {
            LOGGER.info(message);
        }
    }

    private record ItemInfo(String id, String modId) {}
}