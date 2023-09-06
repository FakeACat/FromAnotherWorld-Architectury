package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.statuseffects.SlowAssimilationStatusEffect;
import net.minecraft.world.effect.MobEffect;

public class StatusEffectRegistry {
    public static final FALRegister<MobEffect> STATUS_EFFECT_REGISTRY = new FALRegister<>();

    public static final FALRegistryObject<SlowAssimilationStatusEffect> SLOW_ASSIMILATION = STATUS_EFFECT_REGISTRY.register("slow_assimilation", SlowAssimilationStatusEffect::new);
}
