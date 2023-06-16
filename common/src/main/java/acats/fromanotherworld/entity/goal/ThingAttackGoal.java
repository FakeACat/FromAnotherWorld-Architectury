package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;

public class ThingAttackGoal extends MeleeAttackGoal {
    protected final Thing mob;
    public ThingAttackGoal(Thing mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
        if (this.mob.canSpit){
            double d = this.getAttackReachSqr(target);
            if (this.getTicksUntilNextAttack() <= 0){
                if (squaredDistance <= d) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                }
                else{
                    AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.mob.level(), this.mob);
                    assimilationLiquid.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(assimilationLiquid.position()).normalize());
                    this.mob.level().addFreshEntity(assimilationLiquid);
                    this.resetAttackCooldown();
                }
            }
        }
        else{
            super.checkAndPerformAttack(target, squaredDistance);
        }

    }
}
