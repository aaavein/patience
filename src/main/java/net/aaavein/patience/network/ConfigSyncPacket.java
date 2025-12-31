package net.aaavein.patience.network;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.aaavein.patience.config.PatienceConfig;
import net.aaavein.patience.handler.CraftingHandler;

import java.util.function.Supplier;

public class ConfigSyncPacket {
    private static final Logger LOGGER = LogManager.getLogger(ConfigSyncPacket.class);
    private static final Gson GSON = new Gson();

    private final PatienceConfig config;

    public ConfigSyncPacket(PatienceConfig config) {
        this.config = config;
    }

    public static void encode(ConfigSyncPacket msg, FriendlyByteBuf buf) {
        try {
            buf.writeUtf(GSON.toJson(msg.config));
        } catch (Exception e) {
            LOGGER.error("Failed to encode config", e);
            buf.writeUtf("{}");
        }
    }

    public static ConfigSyncPacket decode(FriendlyByteBuf buf) {
        try {
            return new ConfigSyncPacket(GSON.fromJson(buf.readUtf(), PatienceConfig.class));
        } catch (Exception e) {
            LOGGER.error("Failed to decode config", e);
            return new ConfigSyncPacket(null);
        }
    }

    public static void handle(ConfigSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.config != null) {
                CraftingHandler.getInstance().setConfig(msg.config);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}