package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PalmerAttackGoal extends ThingAttackGoal {
    protected final PalmerThingEntity mob;
    public PalmerAttackGoal(PalmerThingEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
    }

    int leapCooldown;

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.mob.getTarget();
        this.leapCooldown--;
        if (target != null && this.leapCooldown <= 0 && this.mob.distanceToSqr(target) < 100 && !this.mob.targetGrabbed()){
            Vec3 vel = target.position().add(0, target.getBbHeight() / 2, 0).subtract(mob.position()).normalize().scale(2.0F).add(0, 0.2F, 0);
            mob.setDeltaMovement(vel);
            this.leapCooldown = 100;
        }
    }
}
