package net.aaavein.patience.client;

import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.aaavein.patience.Patience;
import net.aaavein.patience.handler.CraftingHandler;

@Mod.EventBusSubscriber(modid = Patience.MOD_ID, value = Dist.CLIENT)
public final class CameraHandler {
    private static final RandomSource RANDOM = RandomSource.create();

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        float shake = CraftingHandler.getInstance().getCurrentShake();
        if (shake > 0) {
            float pitchOffset = (RANDOM.nextFloat() - 0.5F) * shake;
            float yawOffset = (RANDOM.nextFloat() - 0.5F) * shake;

            event.setPitch(event.getPitch() + pitchOffset);
            event.setYaw(event.getYaw() + yawOffset);
        }
    }

    private CameraHandler() {}
}