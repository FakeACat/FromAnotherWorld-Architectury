package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.ThingEntity;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class ThingAttackGoal extends MeleeAttackGoal {
    protected final ThingEntity mob;
    public ThingAttackGoal(ThingEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {
        if (this.mob.canSpit){
            double d = this.getSquaredMaxAttackDistance(target);
            if (this.getCooldown() <= 0){
                if (squaredDistance <= d) {
                    this.resetCooldown();
                    this.mob.swingHand(Hand.MAIN_HAND);
                    this.mob.tryAttack(target);
                }
                else{
                    AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.mob.getWorld(), this.mob);
                    assimilationLiquid.setVelocity(target.getPos().add(0, target.getHeight() / 2, 0).subtract(assimilationLiquid.getPos()).normalize());
                    this.mob.getWorld().spawnEntity(assimilationLiquid);
                    this.resetCooldown();
                }
            }
        }
        else{
            super.attack(target, squaredDistance);
        }

    }
}
