package net.aaavein.patience.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CraftingExhaustionPayload(float amount) implements CustomPacketPayload {
    public static final Type<CraftingExhaustionPayload> TYPE = new Type<>(NetworkHandler.CRAFTING_EXHAUSTION);

    public static final StreamCodec<FriendlyByteBuf, CraftingExhaustionPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeFloat(payload.amount),
            buf -> new CraftingExhaustionPayload(buf.readFloat())
    );

    public static void handle(CraftingExhaustionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.causeFoodExhaustion(payload.amount);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}