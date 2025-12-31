package net.aaavein.patience.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.aaavein.patience.Patience;

@Mod.EventBusSubscriber(modid = Patience.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Patience.MOD_ID);

    public static final RegistryObject<Attribute> CRAFTING_SPEED = ATTRIBUTES.register("crafting_speed",
            () -> new RangedAttribute("attribute.name.patience.crafting_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, CRAFTING_SPEED.get());
    }

    private AttributeRegistry() {}
}