package acats.fromanotherworld.registry;

import acats.fromanotherworld.statuseffects.SlowAssimilationStatusEffect;
import net.minecraft.entity.effect.StatusEffect;

import java.util.HashMap;
import java.util.function.Supplier;

public class StatusEffectRegistry {
    public static class FAWEffect{
        public FAWEffect(Supplier<StatusEffect> statusEffectSupplier){
            this.statusEffectSupplier = statusEffectSupplier;
        }
        private final Supplier<StatusEffect> statusEffectSupplier;
        private StatusEffect statusEffect;
        public StatusEffect build(){
            this.statusEffect = statusEffectSupplier.get();
            return statusEffect;
        }
        public StatusEffect get(){
            return statusEffect;
        }
    }
    public static final HashMap<String, FAWEffect> STATUS_EFFECT_REGISTRY = new HashMap<>();
    public static final FAWEffect SLOW_ASSIMILATION = registerEffect("slow_assimilation", SlowAssimilationStatusEffect::new);

    private static FAWEffect registerEffect(String id, Supplier<StatusEffect> statusEffectSupplier){
        FAWEffect fawEffect = new FAWEffect(statusEffectSupplier);
        STATUS_EFFECT_REGISTRY.put(id, fawEffect);
        return fawEffect;
    }
}
