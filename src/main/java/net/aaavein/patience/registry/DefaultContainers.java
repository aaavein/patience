package net.aaavein.patience.registry;

import net.aaavein.patience.config.ContainerSettings;
import net.aaavein.patience.util.SlotRange;

import java.util.ArrayList;
import java.util.List;

public final class DefaultContainers {
    private static final List<ContainerSettings> CONTAINERS = new ArrayList<>();

    static {
        register(ContainerSettings.builder()
                .name("minecraft:inventory")
                .screenClass("net.minecraft.client.gui.screens.inventory.InventoryScreen")
                .ingredientSlots(SlotRange.parse("1-4"))
                .overlayTexture("patience:textures/inventory.png")
                .overlayX(134)
                .overlayY(28)
                .overlayWidth(18)
                .overlayHeight(15)
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:inventory")
                .screenClass("top.theillusivec4.curios.client.gui.CuriosScreen")
                .ingredientSlots(SlotRange.parse("1-4"))
                .overlayTexture("patience:textures/inventory.png")
                .overlayX(134)
                .overlayY(28)
                .overlayWidth(18)
                .overlayHeight(15)
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:crafting_table")
                .screenClass("net.minecraft.client.gui.screens.inventory.CraftingScreen")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:smithing_table")
                .screenClass("net.minecraft.client.gui.screens.inventory.SmithingScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .overlayX(67)
                .overlayY(48)
                .craftingSound("patience:smithing_table")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:anvil")
                .screenClass("net.minecraft.client.gui.screens.inventory.AnvilScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .overlayX(101)
                .overlayY(47)
                .craftingSound("patience:anvil")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:grindstone")
                .screenClass("net.minecraft.client.gui.screens.inventory.GrindstoneScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .overlayX(94)
                .overlayY(33)
                .craftingSound("patience:grindstone")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:stonecutter")
                .screenClass("net.minecraft.client.gui.screens.inventory.StonecutterScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .showOverlay(false)
                .craftingSound("patience:stonecutter")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:cartography_table")
                .screenClass("net.minecraft.client.gui.screens.inventory.CartographyTableScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .showOverlay(false)
                .craftingSound("patience:cartography_table")
                .build());

        register(ContainerSettings.builder()
                .name("minecraft:loom")
                .screenClass("net.minecraft.client.gui.screens.inventory.LoomScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .showOverlay(false)
                .craftingSound("patience:loom")
                .build());

        register(ContainerSettings.builder()
                .name("sawmill:sawmill")
                .screenClass("net.mehvahdjukaar.sawmill.SawmillScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .showOverlay(false)
                .craftingSound("patience:sawmill")
                .build());

        register(ContainerSettings.builder()
                .name("woodworks:sawmill")
                .screenClass("com.teamabnormals.woodworks.client.gui.screens.inventory.SawmillScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .showOverlay(false)
                .craftingSound("patience:sawmill")
                .build());

        register(ContainerSettings.builder()
                .name("easel_does_it:easel")
                .screenClass("com.dolthhaven.easeldoesit.common.inventory.EaselScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .showOverlay(false)
                .craftingSound("patience:easel")
                .build());

        register(ContainerSettings.builder()
                .name("galosphere:combustion_table")
                .screenClass("net.orcinus.galosphere.client.gui.CombustionTableScreen")
                .ingredientSlots(SlotRange.parse("0-3"))
                .outputSlot(4)
                .showOverlay(false)
                .craftingSound("patience:combustion_table")
                .build());

        register(ContainerSettings.builder()
                .name("clayworks:pottery_table")
                .screenClass("com.teamabnormals.clayworks.client.gui.screens.inventory.PotteryScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .showOverlay(false)
                .craftingSound("patience:pottery_table")
                .build());

        register(ContainerSettings.builder()
                .name("cold_sweat:sewing_table")
                .screenClass("com.momosoftworks.coldsweat.client.gui.SewingScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .showOverlay(false)
                .craftingSound("patience:sewing_table")
                .build());
    }

    public static void register(ContainerSettings settings) {
        CONTAINERS.add(settings);
    }

    public static List<ContainerSettings> getAll() {
        return new ArrayList<>(CONTAINERS);
    }

    private DefaultContainers() {}
}