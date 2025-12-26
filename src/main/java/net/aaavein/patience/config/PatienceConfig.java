package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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