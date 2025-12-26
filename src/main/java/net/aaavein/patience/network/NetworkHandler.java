package net.aaavein.patience.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.aaavein.patience.Patience;

public final class NetworkHandler {
    public static final ResourceLocation CONFIG_SYNC =
            ResourceLocation.fromNamespaceAndPath(Patience.MOD_ID, "config_sync");

    public static final ResourceLocation CRAFTING_EXHAUSTION =
            ResourceLocation.fromNamespaceAndPath(Patience.MOD_ID, "crafting_exhaustion");

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Patience.MOD_ID).versioned("1.0.0");

        registrar.playToClient(
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.STREAM_CODEC,
                ConfigSyncPayload::handle
        );

        registrar.playToServer(
                CraftingExhaustionPayload.TYPE,
                CraftingExhaustionPayload.STREAM_CODEC,
                CraftingExhaustionPayload::handle
        );
    }

    private NetworkHandler() {}
}