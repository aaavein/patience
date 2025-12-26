package net.aaavein.patience.network;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.aaavein.patience.config.PatienceConfig;
import net.aaavein.patience.handler.CraftingHandler;
import org.jetbrains.annotations.NotNull;

public record ConfigSyncPayload(PatienceConfig config) implements CustomPacketPayload {
    private static final Logger LOGGER = LogManager.getLogger(ConfigSyncPayload.class);
    private static final Gson GSON = new Gson();

    public static final Type<ConfigSyncPayload> TYPE = new Type<>(NetworkHandler.CONFIG_SYNC);

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = StreamCodec.of(
            ConfigSyncPayload::encode,
            ConfigSyncPayload::decode
    );

    private static void encode(FriendlyByteBuf buf, ConfigSyncPayload payload) {
        try {
            buf.writeUtf(GSON.toJson(payload.config));
        } catch (Exception e) {
            LOGGER.error("Failed to encode config", e);
            buf.writeUtf("{}");
        }
    }

    private static ConfigSyncPayload decode(FriendlyByteBuf buf) {
        try {
            return new ConfigSyncPayload(GSON.fromJson(buf.readUtf(), PatienceConfig.class));
        } catch (Exception e) {
            LOGGER.error("Failed to decode config", e);
            return new ConfigSyncPayload(null);
        }
    }

    public static void handle(ConfigSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.config != null) {
                CraftingHandler.getInstance().setConfig(payload.config);
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}