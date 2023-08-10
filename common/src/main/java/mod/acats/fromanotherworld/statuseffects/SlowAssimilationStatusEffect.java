package mod.acats.fromanotherworld.statuseffects;

import mod.acats.fromanotherworld.registry.DamageTypeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SlowAssimilationStatusEffect extends MobEffect {
    public SlowAssimilationStatusEffect() {
        super(MobEffectCategory.HARMFUL, 0x800000);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 200 / (amplifier + 1);
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F || amplifier > 8) {
            entity.hurt(DamageTypeRegistry.assimilation(entity.level()), 1.0F);
        }
    }
}
