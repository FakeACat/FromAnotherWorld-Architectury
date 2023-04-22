package acats.fromanotherworld.statuseffects;

import acats.fromanotherworld.registry.DamageTypeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SlowAssimilationStatusEffect extends StatusEffect {
    public SlowAssimilationStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x800000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 200 / (amplifier + 1);
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F || amplifier > 8) {
            entity.damage(DamageTypeRegistry.assimilation(entity.getWorld()), 1.0F);
        }
    }
}
