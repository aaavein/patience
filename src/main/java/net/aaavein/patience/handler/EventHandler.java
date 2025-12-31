package net.aaavein.patience.handler;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.aaavein.patience.Patience;
import net.aaavein.patience.network.ConfigSyncPacket;
import net.aaavein.patience.network.NetworkHandler;

@Mod.EventBusSubscriber(modid = Patience.MOD_ID)
public final class EventHandler {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        CraftingHandler.getInstance().loadConfig();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ConfigSyncPacket packet = new ConfigSyncPacket(CraftingHandler.getInstance().getConfig());
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player.level().isClientSide()) {
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
                                    NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(),
                                            new ConfigSyncPacket(CraftingHandler.getInstance().getConfig())
                                    );
                                    ctx.getSource().sendSuccess(
                                            () -> Component.translatable("command.patience.reload.success").withStyle(style -> style.withColor(0xFF68C1)),
                                            true
                                    );
                                    return 1;
                                })));
    }

    private EventHandler() {}
}