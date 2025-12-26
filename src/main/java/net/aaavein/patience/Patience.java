package net.aaavein.patience;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.aaavein.patience.client.sound.SoundRegistry;
import net.aaavein.patience.handler.CraftingHandler;
import net.aaavein.patience.network.NetworkHandler;
import net.aaavein.patience.registry.AttributeRegistry;

@Mod(Patience.MOD_ID)
public class Patience {
    public static final String MOD_ID = "patience";

    public Patience(IEventBus modEventBus) {
        SoundRegistry.SOUNDS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(NetworkHandler::register);
        CraftingHandler.initialize();
    }
}