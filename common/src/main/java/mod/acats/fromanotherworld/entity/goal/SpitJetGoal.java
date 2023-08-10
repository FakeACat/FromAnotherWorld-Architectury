package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.registry.SoundRegistry;
import mod.acats.fromanotherworld.utilities.ProjectileUtilities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class SpitJetGoal extends Goal {
    private final int cooldownTicks;
    private final int durationTicks;
    private final int warningTicks;
    private final float rangeSq;
    private final Vec3 offset;
    private final Thing thing;
    public SpitJetGoal(Thing thing, int cooldownTicks, int durationTicks, int warningTicks, float range, Vec3 offset){
        this.cooldownTicks = cooldownTicks;
        this.thing = thing;
        this.durationTicks = durationTicks;
        this.rangeSq = range * range;
        this.warningTicks = warningTicks;
        this.offset = offset;
    }

    int timer;

    @Override
    public void tick() {
        if (++this.timer > this.cooldownTicks){
            this.thing.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
            LivingEntity target = this.thing.getTarget();
            if (target != null){
                ProjectileUtilities.shootFromTo(new AssimilationLiquidEntity(this.thing.level(), this.thing), this.thing, target, 2.0F, this.offset);
            }
            if (this.timer > this.cooldownTicks + this.durationTicks){
                this.timer = 0;
            }
        }
        else if (this.timer == this.cooldownTicks - this.warningTicks){
            this.thing.playSound(SoundRegistry.STRONG_AMBIENT.get(), 1.0F, 0.2F);
        }
    }

    @Override
    public void stop() {
        this.timer = 0;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.thing.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.thing.distanceToSqr(livingEntity) < this.rangeSq;
    }
}
