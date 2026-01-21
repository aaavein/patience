package net.aaavein.patience.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
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
import net.aaavein.patience.config.ItemSettings;
import net.aaavein.patience.config.PatienceConfig;
import net.aaavein.patience.config.RecipeSettings;
import net.aaavein.patience.network.CraftingExhaustionPayload;
import net.aaavein.patience.registry.AttributeRegistry;
import net.aaavein.patience.util.IngredientCountHelper;
import net.aaavein.patience.util.SlotRange;
import net.aaavein.patience.util.SpeedCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CraftingHandler {
    private static final Logger LOGGER = LogManager.getLogger(CraftingHandler.class);
    private static final float BASE_CRAFT_TIME = 20.0F;
    private static final double MOVEMENT_THRESHOLD = 0.01;
    private static final double VELOCITY_THRESHOLD = 0.01;
    private static final int SOUND_PITCH_INTERVAL = 25;
    private static final RandomSource RANDOM = RandomSource.create();
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

    private List<ItemStack> lockedIngredients;

    private CraftingSoundInstance currentSound;
    private String cachedItemSound;
    private int soundTicks;
    private float currentShake;

    private boolean miniGameActive;
    private float miniGameStart;
    private float miniGameEnd;
    private int resultTimer;
    private int resultState;

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

    public float getCurrentShake() {
        return currentShake;
    }

    public void setScreen(Object screen) {
        this.currentScreen = (AbstractContainerScreen<?>) screen;
    }

    public void clearScreen() {
        this.currentScreen = null;
        stopCrafting();
    }

    public int getResultState() {
        return resultState;
    }

    public boolean isMiniGameActive() {
        return miniGameActive;
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
            return false;
        }

        if (isSlotEmpty(container.getOutputSlot())) {
            return true;
        }

        if (crafting && miniGameActive && resultState == 0) {
            checkMiniGame();
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

    public float[] getOverlayColor() {
        if (resultState == 1) {
            return new float[]{0.0F, 1.0F, 0.0F, 1.0F};
        } else if (resultState == 2) {
            return new float[]{1.0F, 0.0F, 0.0F, 1.0F};
        } else if (miniGameActive) {
            float progress = currentTime / totalTime;
            if (progress >= miniGameStart && progress <= miniGameEnd) {
                return new float[]{1.0F, 1.0F, 0.0F, 1.0F};
            }
        }
        return new float[]{1.0F, 1.0F, 1.0F, 1.0F};
    }

    private void checkMiniGame() {
        float progress = currentTime / totalTime;
        if (progress >= miniGameStart && progress <= miniGameEnd) {
            resultState = 1;
            resultTimer = 20;
            currentTime = totalTime;
        } else {
            resultState = 2;
            resultTimer = 20;
            currentTime = Math.max(0, currentTime - (totalTime * config.getMinigame().getPenaltyPercent()));
        }
    }

    private void setupMiniGame() {
        this.miniGameActive = false;
        this.resultState = 0;
        this.resultTimer = 0;

        if (config.getMinigame().isEnabled() && RANDOM.nextFloat() < config.getMinigame().getChance()) {
            this.miniGameActive = true;
            float width = config.getMinigame().getWindowWidth();
            float safeMaxStart = Math.max(0.2F, 0.9F - width);
            float range = safeMaxStart - 0.2F;

            if (range <= 0) {
                this.miniGameStart = 0.2F;
            } else {
                this.miniGameStart = 0.2F + RANDOM.nextFloat() * range;
            }

            this.miniGameEnd = this.miniGameStart + width;
        }
    }

    private void startCrafting(ContainerSettings container, boolean continuous) {
        this.currentContainer = container;
        this.continuous = continuous;
        this.crafting = true;

        this.lockedIngredients = getIngredientSnapshot(container.getIngredientSlots());

        float newTotalTime = calculateCraftTime(container);
        if (!config.getDecay().isEnabled() || currentTime > newTotalTime) {
            this.currentTime = 0;
        }

        this.totalTime = newTotalTime;

        setupMiniGame();

        recordPosition();

        if (totalTime >= 10.0F && config.isSoundsEnabled()) {
            playCraftingSound(getEffectiveCraftingSound(container));
        }
    }

    private void stopCrafting() {
        stopCrafting(false);
    }

    private void stopCrafting(boolean forceReset) {
        this.crafting = false;
        this.continuous = false;
        this.currentContainer = null;
        this.currentShake = 0;
        this.miniGameActive = false;
        this.resultState = 0;
        this.resultTimer = 0;
        this.cachedItemSound = null;
        this.lockedIngredients = null;

        if (forceReset || !config.getDecay().isEnabled()) {
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

        if (currentShake > 0) {
            currentShake -= 0.02F;
            if (currentShake < 0) currentShake = 0;
        }

        if (resultTimer > 0) {
            resultTimer--;
            if (resultTimer == 0) {
                resultState = 0;
            }
        }

        ContainerSettings container = getCurrentContainerSettings();

        if (!container.isEnabled()) {
            if (config.getDecay().isEnabled() && currentTime > 0) {
                decayProgress();
            } else {
                currentTime = 0;
            }
            return;
        }

        if (crafting) {
            tickSound();
            tickCrafting(container);
        } else if (config.getDecay().isEnabled() && currentTime > 0) {
            decayProgress();
        }
    }

    private void decayProgress() {
        currentTime -= config.getDecay().getRate();
        currentShake = 0;
        if (currentTime <= 0) {
            currentTime = 0;
            stopCrafting();
        }
    }

    private void tickCrafting(ContainerSettings container) {
        if (config.getDecay().isEnabled()) {
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

        if (lockedIngredients != null) {
            List<ItemStack> currentIngredients = getIngredientSnapshot(container.getIngredientSlots());
            if (!areIngredientsEqual(lockedIngredients, currentIngredients)) {
                logDebug("Ingredients changed during craft, cancelling");
                stopCrafting(true);
                return;
            }
        }

        int outputSlot = container.getOutputSlot();

        if (isSlotEmpty(outputSlot)) {
            if (++waitTicks > 5) {
                waitTicks = 0;
                stopCrafting(true);
            }
            return;
        }

        if (currentTime < totalTime) {
            double attributeValue = 1.0;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                attributeValue = player.getAttributeValue(AttributeRegistry.CRAFTING_SPEED);
            }

            float speed = SpeedCalculator.getCraftingSpeed(
                    attributeValue,
                    getPlayerLevel(),
                    config.getExperience().getBaseSpeed(),
                    config.getExperience().getSpeedPerLevel(),
                    config.getExperience().getMaxLevelCap()
            );

            float hungerMult = 1.0F;
            if (config.getHunger().isPenaltyEnabled() && player != null) {
                if (player.getFoodData().getFoodLevel() <= config.getHunger().getThreshold()) {
                    hungerMult = config.getHunger().getPenaltyMultiplier();
                }
            }

            currentTime += speed * config.getExperience().getMultiplier() * hungerMult;
        } else {
            completeCraft(container);
        }
    }

    private void completeCraft(ContainerSettings container) {
        if (config.getHunger().getExhaustionCost() > 0) {
            PacketDistributor.sendToServer(new CraftingExhaustionPayload(config.getHunger().getExhaustionCost()));
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
                stopCrafting(true);
            } else {
                waitTicks = 0;
                currentTime = 0;

                this.resultState = 0;
                this.resultTimer = 0;

                startCrafting(container, true);
            }
        } else {
            stopCrafting(true);
        }
    }

    private float calculateCraftTime(ContainerSettings container) {
        float globalMult = config.getGlobalTimeMultiplier();
        float containerMult = container.getTimeMultiplier();

        String countMode = container.getIngredientCountMode();
        if (countMode == null) {
            countMode = "slot";
        }

        int recipeIngredientCount = 1;
        if ("recipe".equals(countMode)) {
            recipeIngredientCount = IngredientCountHelper.getRecipeIngredientCount(currentScreen.getMenu());
        }

        float ingredientTime = 0.0F;
        for (int slot : container.getIngredientSlots()) {
            if (isSlotEmpty(slot)) continue;

            ItemStack stack = currentScreen.getMenu().getSlot(slot).getItem();
            if (stack.isEmpty()) continue;

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String modId = id.getNamespace();
            String itemId = id.toString();

            float modMult = config.getIngredientMultipliers().getByMod().getOrDefault(modId, 1.0F);
            float itemMult = config.getIngredientMultipliers().getByItem().getOrDefault(itemId, 1.0F);
            float tagMult = getTagMultiplier(stack, config.getIngredientMultipliers());

            float itemContribution = modMult * itemMult * tagMult;

            switch (countMode) {
                case "stack":
                    ingredientTime += itemContribution * stack.getCount();
                    break;
                case "recipe":
                    ingredientTime += itemContribution * recipeIngredientCount;
                    break;
                default:
                    ingredientTime += itemContribution;
                    break;
            }
        }

        float outputMult = 1.0F;
        ItemStack outputStack = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
        if (!outputStack.isEmpty()) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(outputStack.getItem());
            String modId = id.getNamespace();
            String itemId = id.toString();

            float modMult = config.getOutputMultipliers().getByMod().getOrDefault(modId, 1.0F);
            float itemMult = config.getOutputMultipliers().getByItem().getOrDefault(itemId, 1.0F);
            float tagMult = getTagMultiplier(outputStack, config.getOutputMultipliers());

            outputMult = modMult * itemMult * tagMult;
        }

        float recipeMult = getRecipeMultiplier(container);

        return BASE_CRAFT_TIME * ingredientTime * outputMult * recipeMult * containerMult * globalMult;
    }

    private ResourceLocation getAutomaticRecipeType() {
        if (currentScreen == null) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
        AbstractContainerMenu menu = currentScreen.getMenu();

        if (menu instanceof CraftingMenu || menu instanceof InventoryMenu) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
        return switch (menu) {
            case StonecutterMenu stonecutterMenu -> BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.STONECUTTING);
            case SmithingMenu smithingMenu -> BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.SMITHING);
            case AbstractFurnaceMenu abstractFurnaceMenu -> BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.SMELTING);
            default -> BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
        };

    }

    private float getRecipeMultiplier(ContainerSettings container) {
        if (config.getRecipeMultipliers() == null || currentScreen == null) return 1.0F;

        RecipeSettings settings = config.getRecipeMultipliers();
        if (settings.getByType().isEmpty() && settings.getByRecipe().isEmpty()) return 1.0F;

        String recipeTypeKey = container.getRecipeType();
        if (recipeTypeKey == null || recipeTypeKey.isEmpty()) {
            recipeTypeKey = getAutomaticRecipeType().toString();
        }

        float typeMult = settings.getByType().getOrDefault(recipeTypeKey, 1.0F);

        ItemStack outputStack = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
        if (outputStack.isEmpty()) return typeMult;

        if (!settings.getByRecipe().isEmpty()) {
            try {
                if (Minecraft.getInstance().level != null) {
                    ResourceLocation typeId = ResourceLocation.parse(recipeTypeKey);
                    Optional<RecipeType<?>> typeOpt = BuiltInRegistries.RECIPE_TYPE.getOptional(typeId);

                    if (typeOpt.isPresent()) {
                        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

                        @SuppressWarnings("unchecked")
                        List<RecipeHolder<?>> recipes = (List<RecipeHolder<?>>) (List<?>) recipeManager.getAllRecipesFor((RecipeType) typeOpt.get());

                        for (RecipeHolder<?> holder : recipes) {
                            if (ItemStack.isSameItem(holder.value().getResultItem(Minecraft.getInstance().level.registryAccess()), outputStack)) {
                                String recipeId = holder.id().toString();
                                if (settings.getByRecipe().containsKey(recipeId)) {
                                    return typeMult * settings.getByRecipe().get(recipeId);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return typeMult;
    }

    private float getTagMultiplier(ItemStack stack, ItemSettings settings) {
        float mult = 1.0F;
        if (settings.getByTag() == null || settings.getByTag().isEmpty()) return mult;

        for (var tag : stack.getTags().toList()) {
            String key = "#" + tag.location().toString();
            mult *= settings.getByTag().getOrDefault(key, 1.0F);
        }
        return mult;
    }

    private String getEffectiveCraftingSound(ContainerSettings container) {
        if (currentScreen != null) {
            ItemStack output = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
            if (!output.isEmpty()) {
                String itemSound = getItemSpecificSound(output);
                if (itemSound != null && !itemSound.isEmpty()) {
                    cachedItemSound = itemSound;
                    return itemSound;
                }
                cachedItemSound = null;
            }
        }

        if (cachedItemSound != null) {
            return cachedItemSound;
        }

        String sound = container.getCraftingSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultCraftingSound();
    }

    private String getItemSpecificSound(ItemStack stack) {
        if (config.getItemSounds() == null) return null;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String itemSound = config.getItemSounds().get(itemId.toString());
        if (itemSound != null && !itemSound.isEmpty()) return itemSound;

        for (var tag : stack.getTags().toList()) {
            String tagSound = config.getItemSounds().get("#" + tag.location().toString());
            if (tagSound != null && !tagSound.isEmpty()) return tagSound;
        }

        return null;
    }

    private String getEffectiveFinishSound(ContainerSettings container) {
        String sound = container.getFinishSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultFinishSound();
    }

    @OnlyIn(Dist.CLIENT)
    private void playCraftingSound(String soundId) {
        stopSound();
        soundTicks = 0;
        playSound(soundId);
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(String soundId) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (config.getScreenShake().isEnabled()) {
                currentShake = config.getScreenShake().getIntensity();
            }

            player.swing(InteractionHand.MAIN_HAND);

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

                String soundId = getEffectiveCraftingSound(currentContainer);
                playSound(soundId);
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

            if (cost <= 0) return true;
            return player.getAbilities().instabuild || player.experienceLevel >= cost;
        }

        return true;
    }

    private boolean isSlotEmpty(int slot) {
        if (currentScreen == null) return true;
        return currentScreen.getMenu().getSlot(slot).getItem().isEmpty();
    }

    private Object getSlotItems(SlotRange range) {
        List<Item> items = new ArrayList<>();
        if (currentScreen == null) return items;

        for (int slot : range) {
            items.add(currentScreen.getMenu().getSlot(slot).getItem().getItem());
        }
        return items;
    }

    private List<ItemStack> getIngredientSnapshot(SlotRange range) {
        List<ItemStack> snapshot = new ArrayList<>();
        if (currentScreen == null) return snapshot;

        for (int slot : range) {
            snapshot.add(currentScreen.getMenu().getSlot(slot).getItem().copy());
        }
        return snapshot;
    }

    private boolean areIngredientsEqual(List<ItemStack> original, List<ItemStack> current) {
        if (original.size() != current.size()) return false;

        for (int i = 0; i < original.size(); i++) {
            ItemStack o = original.get(i);
            ItemStack c = current.get(i);

            if (!ItemStack.matches(o, c)) {
                return false;
            }
        }
        return true;
    }

    private void logDebug(String message) {
        if (config != null && config.isDebug()) {
            LOGGER.info(message);
        }
    }
}