package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class StatusEffectRegistryFabric {
    public static void register(){
        StatusEffectRegistry.STATUS_EFFECT_REGISTRY.registerAll(StatusEffectRegistryFabric::registerEffect);
    }

    private static void registerEffect(String id, Supplier<? extends MobEffect> supplier){
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(FromAnotherWorld.MOD_ID, id), supplier.get());
    }
}
