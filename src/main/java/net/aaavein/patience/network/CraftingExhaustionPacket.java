package net.aaavein.patience.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CraftingExhaustionPacket {
    private final float amount;

    public CraftingExhaustionPacket(float amount) {
        this.amount = amount;
    }

    public static void encode(CraftingExhaustionPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.amount);
    }

    public static CraftingExhaustionPacket decode(FriendlyByteBuf buf) {
        return new CraftingExhaustionPacket(buf.readFloat());
    }

    public static void handle(CraftingExhaustionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.causeFoodExhaustion(msg.amount);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}