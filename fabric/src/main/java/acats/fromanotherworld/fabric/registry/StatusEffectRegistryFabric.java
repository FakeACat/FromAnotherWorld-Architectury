package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatusEffectRegistryFabric {
    public static void register(){
        StatusEffectRegistry.STATUS_EFFECT_REGISTRY.forEach(StatusEffectRegistryFabric::registerEffect);
    }

    private static void registerEffect(String id, StatusEffectRegistry.FAWEffect fawEffect){
        Registry.register(Registries.STATUS_EFFECT, new Identifier(FromAnotherWorld.MOD_ID, id), fawEffect.build());
    }
}
