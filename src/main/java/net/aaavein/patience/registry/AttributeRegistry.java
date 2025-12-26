package net.aaavein.patience.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.aaavein.patience.Patience;

@EventBusSubscriber(modid = Patience.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Patience.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> CRAFTING_SPEED = ATTRIBUTES.register("crafting_speed",
            () -> new RangedAttribute("attribute.name.patience.crafting_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, CRAFTING_SPEED);
    }

    private AttributeRegistry() {}
}