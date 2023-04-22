package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesEvents {
    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event){
        EntityRegistry.ATTRIBUTE_REGISTRY.forEach((p, q) -> registerAttributes(p, q, event));
    }

    private static void registerAttributes(String id, Supplier<DefaultAttributeContainer.Builder> attributeSupplier, EntityAttributeCreationEvent event){
        event.put(EntityRegistry.LIVING_ENTITY_REGISTRY.get(id).get(), attributeSupplier.get().build());
    }
}
