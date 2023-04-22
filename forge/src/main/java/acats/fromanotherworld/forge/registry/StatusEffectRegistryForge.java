package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StatusEffectRegistryForge {
    public static final DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FromAnotherWorld.MOD_ID);
    public static void register(IEventBus eventBus){
        StatusEffectRegistry.STATUS_EFFECT_REGISTRY.forEach(StatusEffectRegistryForge::registerEffect);
        STATUS_EFFECTS.register(eventBus);
    }

    private static void registerEffect(String id, StatusEffectRegistry.FAWEffect fawEffect){
        STATUS_EFFECTS.register(id, fawEffect::build);
    }
}
