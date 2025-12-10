package net.aaavein.patience.api;

import net.minecraft.world.inventory.Slot;

/**
 * Interface implemented via mixin on container screens to handle craft completion
 */
public interface CraftingContainer {
    void patience$completeCraft(Slot slot, int slotId);
}