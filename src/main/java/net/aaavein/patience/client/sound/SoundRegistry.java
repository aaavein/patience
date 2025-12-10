package net.aaavein.patience.client.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.aaavein.patience.Patience;

import java.util.function.Supplier;

public final class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Patience.MOD_ID);

    public static final Supplier<SoundEvent> CRAFTING = SOUNDS.register("crafting",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MOD_ID, "crafting")));

    public static final Supplier<SoundEvent> FINISH = SOUNDS.register("finish",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MOD_ID, "finish")));

    private SoundRegistry() {}
}