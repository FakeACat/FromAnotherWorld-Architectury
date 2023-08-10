package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherworld.statuseffects.SlowAssimilationStatusEffect;

import mod.acats.fromanotherworld.utilities.registry.FAWRegister;
import mod.acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.world.effect.MobEffect;

public class StatusEffectRegistry {
    public static final FAWRegister<MobEffect> STATUS_EFFECT_REGISTRY = new FAWRegister<>();
    public static final FAWRegistryObject<SlowAssimilationStatusEffect> SLOW_ASSIMILATION = STATUS_EFFECT_REGISTRY.register("slow_assimilation", SlowAssimilationStatusEffect::new);
}
