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

    @SerializedName("experience_multiplier")
    private float experienceMultiplier;

    @SerializedName("base_crafting_speed")
    private float baseCraftingSpeed;

    @SerializedName("speed_per_level")
    private float speedPerLevel;

    @SerializedName("max_level_cap")
    private int maxLevelCap;

    @SerializedName("exhaustion_cost")
    private float exhaustionCost;

    @SerializedName("enable_decay")
    private boolean enableDecay;

    @SerializedName("decay_rate")
    private float decayRate;

    @SerializedName("enable_screen_shake")
    private boolean enableScreenShake;

    @SerializedName("screen_shake_intensity")
    private float screenShakeIntensity;

    @SerializedName("enable_hunger_penalty")
    private boolean enableHungerPenalty;

    @SerializedName("hunger_threshold")
    private int hungerThreshold;

    @SerializedName("hunger_penalty_multiplier")
    private float hungerPenaltyMultiplier;

    @SerializedName("enable_minigame")
    private boolean enableMinigame;

    @SerializedName("minigame_chance")
    private float minigameChance;

    @SerializedName("minigame_window_width")
    private float minigameWindowWidth;

    @SerializedName("minigame_penalty_percent")
    private float minigamePenaltyPercent;

    @SerializedName("item_sounds")
    private Map<String, String> itemSounds;

    @SerializedName("containers")
    private List<ContainerSettings> containers;

    @SerializedName("ingredient_multipliers")
    private ItemSettings ingredientMultipliers;

    @SerializedName("output_multipliers")
    private ItemSettings outputMultipliers;

    public PatienceConfig() {
        this.enableSounds = true;
        this.defaultCraftingSound = "patience:crafting";
        this.defaultFinishSound = "patience:finish";
        this.globalTimeMultiplier = 1.0F;
        this.experienceMultiplier = 1.0F;
        this.baseCraftingSpeed = 1.0F;
        this.speedPerLevel = 0.02F;
        this.maxLevelCap = 200;
        this.exhaustionCost = 0.1F;
        this.enableDecay = true;
        this.decayRate = 2.0F;
        this.enableScreenShake = true;
        this.screenShakeIntensity = 0.5F;
        this.enableHungerPenalty = true;
        this.hungerThreshold = 6;
        this.hungerPenaltyMultiplier = 0.5F;
        this.enableMinigame = true;
        this.minigameChance = 0.5F;
        this.minigameWindowWidth = 0.15F;
        this.minigamePenaltyPercent = 0.25F;
        this.itemSounds = new HashMap<>();
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isSoundsEnabled() {
        return enableSounds;
    }

    public String getDefaultCraftingSound() {
        return defaultCraftingSound;
    }

    public String getDefaultFinishSound() {
        return defaultFinishSound;
    }

    public float getGlobalTimeMultiplier() {
        return globalTimeMultiplier;
    }

    public float getExperienceMultiplier() {
        return experienceMultiplier;
    }

    public float getBaseCraftingSpeed() {
        return baseCraftingSpeed;
    }

    public float getSpeedPerLevel() {
        return speedPerLevel;
    }

    public int getMaxLevelCap() {
        return maxLevelCap;
    }

    public float getExhaustionCost() {
        return exhaustionCost;
    }

    public boolean isDecayEnabled() {
        return enableDecay;
    }

    public float getDecayRate() {
        return decayRate;
    }

    public boolean isScreenShakeEnabled() {
        return enableScreenShake;
    }

    public float getScreenShakeIntensity() {
        return screenShakeIntensity;
    }

    public boolean isHungerPenaltyEnabled() {
        return enableHungerPenalty;
    }

    public int getHungerThreshold() {
        return hungerThreshold;
    }

    public float getHungerPenaltyMultiplier() {
        return hungerPenaltyMultiplier;
    }

    public boolean isMinigameEnabled() {
        return enableMinigame;
    }

    public float getMinigameChance() {
        return minigameChance;
    }

    public float getMinigameWindowWidth() {
        return minigameWindowWidth;
    }

    public float getMinigamePenaltyPercent() {
        return minigamePenaltyPercent;
    }

    public Map<String, String> getItemSounds() {
        return itemSounds;
    }

    public List<ContainerSettings> getContainers() {
        return containers;
    }

    public ItemSettings getIngredientMultipliers() {
        return ingredientMultipliers;
    }

    public ItemSettings getOutputMultipliers() {
        return outputMultipliers;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setEnableSounds(boolean enableSounds) {
        this.enableSounds = enableSounds;
    }

    public void setDefaultCraftingSound(String defaultCraftingSound) {
        this.defaultCraftingSound = defaultCraftingSound;
    }

    public void setDefaultFinishSound(String defaultFinishSound) {
        this.defaultFinishSound = defaultFinishSound;
    }

    public void setGlobalTimeMultiplier(float globalTimeMultiplier) {
        this.globalTimeMultiplier = globalTimeMultiplier;
    }

    public void setExperienceMultiplier(float experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
    }

    public void setBaseCraftingSpeed(float baseCraftingSpeed) {
        this.baseCraftingSpeed = baseCraftingSpeed;
    }

    public void setSpeedPerLevel(float speedPerLevel) {
        this.speedPerLevel = speedPerLevel;
    }

    public void setMaxLevelCap(int maxLevelCap) {
        this.maxLevelCap = maxLevelCap;
    }

    public void setExhaustionCost(float exhaustionCost) {
        this.exhaustionCost = exhaustionCost;
    }

    public void setEnableDecay(boolean enableDecay) {
        this.enableDecay = enableDecay;
    }

    public void setDecayRate(float decayRate) {
        this.decayRate = decayRate;
    }

    public void setEnableScreenShake(boolean enableScreenShake) {
        this.enableScreenShake = enableScreenShake;
    }

    public void setScreenShakeIntensity(float screenShakeIntensity) {
        this.screenShakeIntensity = screenShakeIntensity;
    }

    public void setEnableHungerPenalty(boolean enableHungerPenalty) {
        this.enableHungerPenalty = enableHungerPenalty;
    }

    public void setHungerThreshold(int hungerThreshold) {
        this.hungerThreshold = hungerThreshold;
    }

    public void setHungerPenaltyMultiplier(float hungerPenaltyMultiplier) {
        this.hungerPenaltyMultiplier = hungerPenaltyMultiplier;
    }

    public void setEnableMinigame(boolean enableMinigame) {
        this.enableMinigame = enableMinigame;
    }

    public void setMinigameChance(float minigameChance) {
        this.minigameChance = minigameChance;
    }

    public void setMinigameWindowWidth(float minigameWindowWidth) {
        this.minigameWindowWidth = minigameWindowWidth;
    }

    public void setMinigamePenaltyPercent(float minigamePenaltyPercent) {
        this.minigamePenaltyPercent = minigamePenaltyPercent;
    }

    public void setItemSounds(Map<String, String> itemSounds) {
        this.itemSounds = itemSounds;
    }

    public void setContainers(List<ContainerSettings> containers) {
        this.containers = containers;
    }

    public void setIngredientMultipliers(ItemSettings ingredientMultipliers) {
        this.ingredientMultipliers = ingredientMultipliers;
    }

    public void setOutputMultipliers(ItemSettings outputMultipliers) {
        this.outputMultipliers = outputMultipliers;
    }

    @Override
    public String toString() {
        return "PatienceConfig{" +
                "debug=" + debug +
                ", enable_sounds=" + enableSounds +
                ", global_time_multiplier=" + globalTimeMultiplier +
                ", experience_multiplier=" + experienceMultiplier +
                ", base_speed=" + baseCraftingSpeed +
                ", exhaustion_cost=" + exhaustionCost +
                ", enable_decay=" + enableDecay +
                ", enable_shake=" + enableScreenShake +
                ", enable_hunger_penalty=" + enableHungerPenalty +
                ", enable_minigame=" + enableMinigame +
                ", item_sounds=" + (itemSounds != null ? itemSounds.size() : 0) +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PatienceConfig config = new PatienceConfig();

        public Builder debug(boolean debug) {
            config.setDebug(debug);
            return this;
        }

        public Builder enableSounds(boolean enableSounds) {
            config.setEnableSounds(enableSounds);
            return this;
        }

        public Builder defaultCraftingSound(String sound) {
            config.setDefaultCraftingSound(sound);
            return this;
        }

        public Builder defaultFinishSound(String sound) {
            config.setDefaultFinishSound(sound);
            return this;
        }

        public Builder globalTimeMultiplier(float multiplier) {
            config.setGlobalTimeMultiplier(multiplier);
            return this;
        }

        public Builder experienceMultiplier(float multiplier) {
            config.setExperienceMultiplier(multiplier);
            return this;
        }

        public Builder baseCraftingSpeed(float speed) {
            config.setBaseCraftingSpeed(speed);
            return this;
        }

        public Builder speedPerLevel(float speed) {
            config.setSpeedPerLevel(speed);
            return this;
        }

        public Builder maxLevelCap(int level) {
            config.setMaxLevelCap(level);
            return this;
        }

        public Builder exhaustionCost(float cost) {
            config.setExhaustionCost(cost);
            return this;
        }

        public Builder enableDecay(boolean enable) {
            config.setEnableDecay(enable);
            return this;
        }

        public Builder decayRate(float rate) {
            config.setDecayRate(rate);
            return this;
        }

        public Builder enableScreenShake(boolean enable) {
            config.setEnableScreenShake(enable);
            return this;
        }

        public Builder screenShakeIntensity(float intensity) {
            config.setScreenShakeIntensity(intensity);
            return this;
        }

        public Builder enableHungerPenalty(boolean enable) {
            config.setEnableHungerPenalty(enable);
            return this;
        }

        public Builder hungerThreshold(int threshold) {
            config.setHungerThreshold(threshold);
            return this;
        }

        public Builder hungerPenaltyMultiplier(float multiplier) {
            config.setHungerPenaltyMultiplier(multiplier);
            return this;
        }

        public Builder enableMinigame(boolean enable) {
            config.setEnableMinigame(enable);
            return this;
        }

        public Builder minigameChance(float chance) {
            config.setMinigameChance(chance);
            return this;
        }

        public Builder minigameWindowWidth(float width) {
            config.setMinigameWindowWidth(width);
            return this;
        }

        public Builder minigamePenaltyPercent(float percent) {
            config.setMinigamePenaltyPercent(percent);
            return this;
        }

        public Builder itemSounds(Map<String, String> sounds) {
            config.setItemSounds(sounds);
            return this;
        }

        public Builder containers(List<ContainerSettings> containers) {
            config.setContainers(containers);
            return this;
        }

        public Builder ingredientMultipliers(ItemSettings multipliers) {
            config.setIngredientMultipliers(multipliers);
            return this;
        }

        public Builder outputMultipliers(ItemSettings multipliers) {
            config.setOutputMultipliers(multipliers);
            return this;
        }

        public PatienceConfig build() {
            return config;
        }
    }
}