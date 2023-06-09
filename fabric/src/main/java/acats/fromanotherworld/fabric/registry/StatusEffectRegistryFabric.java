package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class StatusEffectRegistryFabric {
    public static void register(){
        StatusEffectRegistry.STATUS_EFFECT_REGISTRY.forEach(StatusEffectRegistryFabric::registerEffect);
    }

    private static void registerEffect(String id, StatusEffectRegistry.FAWEffect fawEffect){
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(FromAnotherWorld.MOD_ID, id), fawEffect.build());
    }
}
