package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatienceConfig {

    @SerializedName("debug")
    private boolean debug;

    @SerializedName("enable_sounds")
    private boolean enableSounds;

    @SerializedName("default_crafting_sound")
    private String defaultCraftingSound;

    @SerializedName("default_finish_sound")
    private String defaultFinishSound;

    @SerializedName("global_time_multiplier")
    private float globalTimeMultiplier;

    @SerializedName("experience")
    private ExperienceSettings experience;

    @SerializedName("decay")
    private DecaySettings decay;

    @SerializedName("screen_shake")
    private ScreenShakeSettings screenShake;

    @SerializedName("hunger")
    private HungerSettings hunger;

    @SerializedName("minigame")
    private MinigameSettings minigame;

    @SerializedName("item_sounds")
    private Map<String, String> itemSounds;

    @SerializedName("containers")
    private List<ContainerSettings> containers;

    @SerializedName("ingredient_multipliers")
    private ItemSettings ingredientMultipliers;

    @SerializedName("output_multipliers")
    private ItemSettings outputMultipliers;

    public PatienceConfig() {
        this.debug = false;
        this.enableSounds = true;
        this.defaultCraftingSound = "patience:crafting";
        this.defaultFinishSound = "patience:finish";
        this.globalTimeMultiplier = 1.0F;

        this.experience = new ExperienceSettings();
        this.decay = new DecaySettings();
        this.screenShake = new ScreenShakeSettings();
        this.hunger = new HungerSettings();
        this.minigame = new MinigameSettings();

        this.itemSounds = new HashMap<>();
    }

    public static class ExperienceSettings {
        @SerializedName("multiplier")
        private float multiplier = 1.0F;
        @SerializedName("base_speed")
        private float baseSpeed = 1.0F;
        @SerializedName("speed_per_level")
        private float speedPerLevel = 0.02F;
        @SerializedName("max_level_cap")
        private int maxLevelCap = 200;

        public float getMultiplier() { return multiplier; }
        public float getBaseSpeed() { return baseSpeed; }
        public float getSpeedPerLevel() { return speedPerLevel; }
        public int getMaxLevelCap() { return maxLevelCap; }

        public void setMultiplier(float val) { this.multiplier = val; }
        public void setBaseSpeed(float val) { this.baseSpeed = val; }
        public void setSpeedPerLevel(float val) { this.speedPerLevel = val; }
        public void setMaxLevelCap(int val) { this.maxLevelCap = val; }
    }

    public static class DecaySettings {
        @SerializedName("enabled")
        private boolean enabled = true;
        @SerializedName("rate")
        private float rate = 2.0F;

        public boolean isEnabled() { return enabled; }
        public float getRate() { return rate; }

        public void setEnabled(boolean val) { this.enabled = val; }
        public void setRate(float val) { this.rate = val; }
    }

    public static class ScreenShakeSettings {
        @SerializedName("enabled")
        private boolean enabled = true;
        @SerializedName("intensity")
        private float intensity = 0.5F;

        public boolean isEnabled() { return enabled; }
        public float getIntensity() { return intensity; }

        public void setEnabled(boolean val) { this.enabled = val; }
        public void setIntensity(float val) { this.intensity = val; }
    }

    public static class HungerSettings {
        @SerializedName("exhaustion_cost")
        private float exhaustionCost = 0.1F;
        @SerializedName("penalty_enabled")
        private boolean penaltyEnabled = true;
        @SerializedName("threshold")
        private int threshold = 6;
        @SerializedName("penalty_multiplier")
        private float penaltyMultiplier = 0.5F;

        public float getExhaustionCost() { return exhaustionCost; }
        public boolean isPenaltyEnabled() { return penaltyEnabled; }
        public int getThreshold() { return threshold; }
        public float getPenaltyMultiplier() { return penaltyMultiplier; }

        public void setExhaustionCost(float val) { this.exhaustionCost = val; }
        public void setPenaltyEnabled(boolean val) { this.penaltyEnabled = val; }
        public void setThreshold(int val) { this.threshold = val; }
        public void setPenaltyMultiplier(float val) { this.penaltyMultiplier = val; }
    }

    public static class MinigameSettings {
        @SerializedName("enabled")
        private boolean enabled = true;
        @SerializedName("chance")
        private float chance = 0.5F;
        @SerializedName("window_width")
        private float windowWidth = 0.15F;
        @SerializedName("penalty_percent")
        private float penaltyPercent = 0.25F;

        public boolean isEnabled() { return enabled; }
        public float getChance() { return chance; }
        public float getWindowWidth() { return windowWidth; }
        public float getPenaltyPercent() { return penaltyPercent; }

        public void setEnabled(boolean val) { this.enabled = val; }
        public void setChance(float val) { this.chance = val; }
        public void setWindowWidth(float val) { this.windowWidth = val; }
        public void setPenaltyPercent(float val) { this.penaltyPercent = val; }
    }

    public boolean isDebug() { return debug; }
    public boolean isSoundsEnabled() { return enableSounds; }
    public String getDefaultCraftingSound() { return defaultCraftingSound; }
    public String getDefaultFinishSound() { return defaultFinishSound; }
    public float getGlobalTimeMultiplier() { return globalTimeMultiplier; }

    public ExperienceSettings getExperience() { return experience; }
    public DecaySettings getDecay() { return decay; }
    public ScreenShakeSettings getScreenShake() { return screenShake; }
    public HungerSettings getHunger() { return hunger; }
    public MinigameSettings getMinigame() { return minigame; }

    public Map<String, String> getItemSounds() { return itemSounds; }
    public List<ContainerSettings> getContainers() { return containers; }
    public ItemSettings getIngredientMultipliers() { return ingredientMultipliers; }
    public ItemSettings getOutputMultipliers() { return outputMultipliers; }

    public void setDebug(boolean debug) { this.debug = debug; }
    public void setEnableSounds(boolean enableSounds) { this.enableSounds = enableSounds; }
    public void setDefaultCraftingSound(String defaultCraftingSound) { this.defaultCraftingSound = defaultCraftingSound; }
    public void setDefaultFinishSound(String defaultFinishSound) { this.defaultFinishSound = defaultFinishSound; }
    public void setGlobalTimeMultiplier(float globalTimeMultiplier) { this.globalTimeMultiplier = globalTimeMultiplier; }

    public void setExperience(ExperienceSettings experience) { this.experience = experience; }
    public void setDecay(DecaySettings decay) { this.decay = decay; }
    public void setScreenShake(ScreenShakeSettings screenShake) { this.screenShake = screenShake; }
    public void setHunger(HungerSettings hunger) { this.hunger = hunger; }
    public void setMinigame(MinigameSettings minigame) { this.minigame = minigame; }

    public void setItemSounds(Map<String, String> itemSounds) { this.itemSounds = itemSounds; }
    public void setContainers(List<ContainerSettings> containers) { this.containers = containers; }
    public void setIngredientMultipliers(ItemSettings ingredientMultipliers) { this.ingredientMultipliers = ingredientMultipliers; }
    public void setOutputMultipliers(ItemSettings outputMultipliers) { this.outputMultipliers = outputMultipliers; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final PatienceConfig config = new PatienceConfig();

        public Builder debug(boolean val) { config.setDebug(val); return this; }
        public Builder enableSounds(boolean val) { config.setEnableSounds(val); return this; }
        public Builder defaultCraftingSound(String val) { config.setDefaultCraftingSound(val); return this; }
        public Builder defaultFinishSound(String val) { config.setDefaultFinishSound(val); return this; }
        public Builder globalTimeMultiplier(float val) { config.setGlobalTimeMultiplier(val); return this; }

        public Builder experience(ExperienceSettings val) { config.setExperience(val); return this; }
        public Builder decay(DecaySettings val) { config.setDecay(val); return this; }
        public Builder screenShake(ScreenShakeSettings val) { config.setScreenShake(val); return this; }
        public Builder hunger(HungerSettings val) { config.setHunger(val); return this; }
        public Builder minigame(MinigameSettings val) { config.setMinigame(val); return this; }

        public Builder itemSounds(Map<String, String> val) { config.setItemSounds(val); return this; }
        public Builder containers(List<ContainerSettings> val) { config.setContainers(val); return this; }
        public Builder ingredientMultipliers(ItemSettings val) { config.setIngredientMultipliers(val); return this; }
        public Builder outputMultipliers(ItemSettings val) { config.setOutputMultipliers(val); return this; }

        public PatienceConfig build() { return config; }
    }
}