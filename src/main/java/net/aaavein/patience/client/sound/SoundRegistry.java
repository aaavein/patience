package net.aaavein.patience.client.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.aaavein.patience.Patience;

public final class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Patience.MOD_ID);

    public static final RegistryObject<SoundEvent> CRAFTING = SOUNDS.register("crafting",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(Patience.MOD_ID, "crafting")));

    public static final RegistryObject<SoundEvent> FINISH = SOUNDS.register("finish",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(Patience.MOD_ID, "finish")));

    private SoundRegistry() {}
}
