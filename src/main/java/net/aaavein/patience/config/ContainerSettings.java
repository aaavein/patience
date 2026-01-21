package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;
import net.aaavein.patience.util.SlotRange;

import java.util.Arrays;

public class ContainerSettings {

    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("screen_class")
    private String screenClass;

    @SerializedName("recipe_type")
    private String recipeType;

    @SerializedName("time_multiplier")
    private float timeMultiplier;

    @SerializedName("output_slot")
    private int outputSlot;

    @SerializedName("result_slot")
    private int resultSlot;

    @SerializedName("ingredient_slots")
    private SlotRange ingredientSlots;

    @SerializedName("ingredient_mode")
    private String ingredientMode;

    @SerializedName("show_overlay")
    private Boolean showOverlay;

    @SerializedName("overlay_texture")
    private String overlayTexture;

    @SerializedName("overlay_direction")
    private String overlayDirection;

    @SerializedName("overlay_x")
    private Integer overlayX;

    @SerializedName("overlay_y")
    private Integer overlayY;

    @SerializedName("overlay_width")
    private Integer overlayWidth;

    @SerializedName("overlay_height")
    private Integer overlayHeight;

    @SerializedName("crafting_sound")
    private String craftingSound;

    @SerializedName("finish_sound")
    private String finishSound;

    public ContainerSettings() {
        this.enabled = true;
        this.timeMultiplier = 1.0F;
        this.outputSlot = 0;
        this.resultSlot = -1;
        this.ingredientSlots = new SlotRange(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        this.ingredientMode = "slot";
        this.showOverlay = null;
        this.overlayTexture = null;
        this.overlayDirection = null;
        this.overlayX = null;
        this.overlayY = null;
        this.overlayWidth = null;
        this.overlayHeight = null;
        this.craftingSound = null;
        this.finishSound = null;
        this.recipeType = null;
    }

    public boolean isEnabled() { return enabled; }
    public String getScreenClass() { return screenClass; }
    public String getRecipeType() { return recipeType; }
    public float getTimeMultiplier() { return timeMultiplier; }
    public int getOutputSlot() { return outputSlot; }
    public int getResultSlot() { return resultSlot; }
    public SlotRange getIngredientSlots() { return ingredientSlots; }
    public String getIngredientMode() { return ingredientMode; }

    public boolean isShowOverlay() {
        return showOverlay != null ? showOverlay : true;
    }

    public String getOverlayTexture() {
        return overlayTexture != null ? overlayTexture : "patience:textures/generic.png";
    }

    public String getOverlayDirection() {
        return overlayDirection != null ? overlayDirection : "right";
    }

    public int getOverlayX() {
        return overlayX != null ? overlayX : 89;
    }

    public int getOverlayY() {
        return overlayY != null ? overlayY : 34;
    }

    public int getOverlayWidth() {
        return overlayWidth != null ? overlayWidth : 24;
    }

    public int getOverlayHeight() {
        return overlayHeight != null ? overlayHeight : 17;
    }

    public String getCraftingSound() {
        return craftingSound != null ? craftingSound : "";
    }

    public String getFinishSound() {
        return finishSound != null ? finishSound : "";
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setScreenClass(String screenClass) { this.screenClass = screenClass; }
    public void setRecipeType(String recipeType) { this.recipeType = recipeType; }
    public void setTimeMultiplier(float timeMultiplier) { this.timeMultiplier = timeMultiplier; }
    public void setOutputSlot(int outputSlot) { this.outputSlot = outputSlot; }
    public void setResultSlot(int resultSlot) { this.resultSlot = resultSlot; }
    public void setIngredientSlots(SlotRange ingredientSlots) { this.ingredientSlots = ingredientSlots; }
    public void setIngredientMode(String ingredientMode) { this.ingredientMode = ingredientMode; }
    public void setShowOverlay(Boolean showOverlay) { this.showOverlay = showOverlay; }
    public void setOverlayTexture(String overlayTexture) { this.overlayTexture = overlayTexture; }
    public void setOverlayDirection(String overlayDirection) { this.overlayDirection = overlayDirection; }
    public void setOverlayX(Integer overlayX) { this.overlayX = overlayX; }
    public void setOverlayY(Integer overlayY) { this.overlayY = overlayY; }
    public void setOverlayWidth(Integer overlayWidth) { this.overlayWidth = overlayWidth; }
    public void setOverlayHeight(Integer overlayHeight) { this.overlayHeight = overlayHeight; }
    public void setCraftingSound(String craftingSound) { this.craftingSound = craftingSound; }
    public void setFinishSound(String finishSound) { this.finishSound = finishSound; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ContainerSettings settings = new ContainerSettings();

        public Builder enabled(boolean enabled) { settings.setEnabled(enabled); return this; }
        public Builder screenClass(String screenClass) { settings.setScreenClass(screenClass); return this; }
        public Builder recipeType(String recipeType) { settings.setRecipeType(recipeType); return this; }
        public Builder timeMultiplier(float multiplier) { settings.setTimeMultiplier(multiplier); return this; }
        public Builder outputSlot(int slot) { settings.setOutputSlot(slot); return this; }
        public Builder resultSlot(int slot) { settings.setResultSlot(slot); return this; }
        public Builder ingredientSlots(SlotRange slots) { settings.setIngredientSlots(slots); return this; }
        public Builder ingredientMode(String mode) { settings.setIngredientMode(mode); return this; }
        public Builder showOverlay(boolean show) { settings.setShowOverlay(show); return this; }
        public Builder overlayTexture(String texture) { settings.setOverlayTexture(texture); return this; }
        public Builder overlayDirection(String direction) { settings.setOverlayDirection(direction); return this; }
        public Builder overlayX(int x) { settings.setOverlayX(x); return this; }
        public Builder overlayY(int y) { settings.setOverlayY(y); return this; }
        public Builder overlayWidth(int width) { settings.setOverlayWidth(width); return this; }
        public Builder overlayHeight(int height) { settings.setOverlayHeight(height); return this; }
        public Builder craftingSound(String sound) { settings.setCraftingSound(sound); return this; }
        public Builder finishSound(String sound) { settings.setFinishSound(sound); return this; }

        public ContainerSettings build() {
            return settings;
        }
    }
}