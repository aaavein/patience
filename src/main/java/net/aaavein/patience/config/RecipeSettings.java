package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class RecipeSettings {

    @SerializedName("by_type")
    private Map<String, Float> byType;

    @SerializedName("by_recipe")
    private Map<String, Float> byRecipe;

    public RecipeSettings() {
        this.byType = new HashMap<>();
        this.byRecipe = new HashMap<>();
    }

    public RecipeSettings(Map<String, Float> byType, Map<String, Float> byRecipe) {
        this.byType = byType;
        this.byRecipe = byRecipe;
    }

    public Map<String, Float> getByType() {
        return byType;
    }

    public void setByType(Map<String, Float> byType) {
        this.byType = byType;
    }

    public Map<String, Float> getByRecipe() {
        return byRecipe;
    }

    public void setByRecipe(Map<String, Float> byRecipe) {
        this.byRecipe = byRecipe;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Float> byType = new HashMap<>();
        private Map<String, Float> byRecipe = new HashMap<>();

        public Builder byType(Map<String, Float> byType) {
            this.byType = byType;
            return this;
        }

        public Builder byRecipe(Map<String, Float> byRecipe) {
            this.byRecipe = byRecipe;
            return this;
        }

        public RecipeSettings build() {
            return new RecipeSettings(byType, byRecipe);
        }
    }
}
