package net.aaavein.patience.api;

import net.minecraft.world.inventory.Slot;

public interface CraftingContainer {
    void patience$completeCraft(Slot slot, int slotId);
}
