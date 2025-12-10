package net.aaavein.patience.handler;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.aaavein.patience.Patience;
import net.aaavein.patience.network.ConfigSyncPayload;

@EventBusSubscriber(modid = Patience.MOD_ID)
public final class EventHandler {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        CraftingHandler.getInstance().loadConfig();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ConfigSyncPayload payload = new ConfigSyncPayload(CraftingHandler.getInstance().getConfig());
            PacketDistributor.sendToPlayer(serverPlayer, payload);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {
            CraftingHandler.getInstance().tick();
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("patience")
                        .then(Commands.literal("reload")
                                .requires(src -> src.hasPermission(2))
                                .executes(ctx -> {
                                    CraftingHandler.getInstance().loadConfig();
                                    PacketDistributor.sendToAllPlayers(
                                            new ConfigSyncPayload(CraftingHandler.getInstance().getConfig())
                                    );
                                    ctx.getSource().sendSuccess(
                                            () -> Component.translatable("command.patience.reload.success").withColor(0xFF68C1),
                                            true
                                    );
                                    return 1;
                                })));
    }

    private EventHandler() {}
}