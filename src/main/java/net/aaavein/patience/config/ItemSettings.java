package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ItemSettings {

    @SerializedName("by_mod")
    private Map<String, Float> byMod;

    @SerializedName("by_item")
    private Map<String, Float> byItem;

    @SerializedName("by_tag")
    private Map<String, Float> byTag;

    public ItemSettings() {
        this.byMod = new HashMap<>();
        this.byItem = new HashMap<>();
        this.byTag = new HashMap<>();
    }

    public ItemSettings(Map<String, Float> byMod, Map<String, Float> byItem, Map<String, Float> byTag) {
        this.byMod = byMod;
        this.byItem = byItem;
        this.byTag = byTag;
    }

    public Map<String, Float> getByMod() {
        return byMod;
    }

    public void setByMod(Map<String, Float> byMod) {
        this.byMod = byMod;
    }

    public Map<String, Float> getByItem() {
        return byItem;
    }

    public void setByItem(Map<String, Float> byItem) {
        this.byItem = byItem;
    }

    public Map<String, Float> getByTag() {
        return byTag;
    }

    public void setByTag(Map<String, Float> byTag) {
        this.byTag = byTag;
    }

    @Override
    public String toString() {
        return "ItemSettings{by_mod=" + byMod.size() +
                ", by_item=" + byItem.size() +
                ", by_tag=" + byTag.size() + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Float> byMod = new HashMap<>();
        private Map<String, Float> byItem = new HashMap<>();
        private Map<String, Float> byTag = new HashMap<>();

        public Builder byMod(Map<String, Float> byMod) {
            this.byMod = byMod;
            return this;
        }

        public Builder byItem(Map<String, Float> byItem) {
            this.byItem = byItem;
            return this;
        }

        public Builder byTag(Map<String, Float> byTag) {
            this.byTag = byTag;
            return this;
        }

        public ItemSettings build() {
            return new ItemSettings(byMod, byItem, byTag);
        }
    }
}
