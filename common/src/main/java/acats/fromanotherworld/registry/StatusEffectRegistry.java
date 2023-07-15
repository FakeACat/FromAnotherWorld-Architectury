package acats.fromanotherworld.registry;

import acats.fromanotherworld.statuseffects.SlowAssimilationStatusEffect;

import acats.fromanotherworld.utilities.registry.FAWRegister;
import acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.world.effect.MobEffect;

public class StatusEffectRegistry {
    public static final FAWRegister<MobEffect> STATUS_EFFECT_REGISTRY = new FAWRegister<>();
    public static final FAWRegistryObject<SlowAssimilationStatusEffect> SLOW_ASSIMILATION = STATUS_EFFECT_REGISTRY.register("slow_assimilation", SlowAssimilationStatusEffect::new);
}
