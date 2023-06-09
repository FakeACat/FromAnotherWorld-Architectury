package acats.fromanotherworld.registry;

import acats.fromanotherworld.statuseffects.SlowAssimilationStatusEffect;
import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffect;

public class StatusEffectRegistry {
    public static class FAWEffect{
        public FAWEffect(Supplier<MobEffect> statusEffectSupplier){
            this.statusEffectSupplier = statusEffectSupplier;
        }
        private final Supplier<MobEffect> statusEffectSupplier;
        private MobEffect statusEffect;
        public MobEffect build(){
            this.statusEffect = statusEffectSupplier.get();
            return statusEffect;
        }
        public MobEffect get(){
            return statusEffect;
        }
    }
    public static final HashMap<String, FAWEffect> STATUS_EFFECT_REGISTRY = new HashMap<>();
    public static final FAWEffect SLOW_ASSIMILATION = registerEffect("slow_assimilation", SlowAssimilationStatusEffect::new);

    private static FAWEffect registerEffect(String id, Supplier<MobEffect> statusEffectSupplier){
        FAWEffect fawEffect = new FAWEffect(statusEffectSupplier);
        STATUS_EFFECT_REGISTRY.put(id, fawEffect);
        return fawEffect;
    }
}
