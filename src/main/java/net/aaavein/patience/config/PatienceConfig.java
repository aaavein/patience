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