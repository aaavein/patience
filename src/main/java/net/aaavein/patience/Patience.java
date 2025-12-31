package net.aaavein.patience;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.aaavein.patience.client.sound.SoundRegistry;
import net.aaavein.patience.handler.CraftingHandler;
import net.aaavein.patience.network.NetworkHandler;
import net.aaavein.patience.registry.AttributeRegistry;

@Mod(Patience.MOD_ID)
public class Patience {
    public static final String MOD_ID = "patience";

    public Patience() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SoundRegistry.SOUNDS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        NetworkHandler.register();
        CraftingHandler.initialize();
    }
}