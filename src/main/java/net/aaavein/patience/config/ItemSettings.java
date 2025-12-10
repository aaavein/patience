package net.aaavein.patience.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ItemSettings {

    @SerializedName("by_mod")
    private Map<String, Float> byMod;

    @SerializedName("by_item")
    private Map<String, Float> byItem;

    public ItemSettings() {
        this.byMod = new HashMap<>();
        this.byItem = new HashMap<>();
    }

    public ItemSettings(Map<String, Float> byMod, Map<String, Float> byItem) {
        this.byMod = byMod;
        this.byItem = byItem;
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

    @Override
    public String toString() {
        return "ItemSettings{by_mod=" + byMod.size() + " entries, by_item=" + byItem.size() + " entries}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Float> byMod = new HashMap<>();
        private Map<String, Float> byItem = new HashMap<>();

        public Builder byMod(Map<String, Float> byMod) {
            this.byMod = byMod;
            return this;
        }

        public Builder byItem(Map<String, Float> byItem) {
            this.byItem = byItem;
            return this;
        }

        public ItemSettings build() {
            return new ItemSettings(byMod, byItem);
        }
    }
}