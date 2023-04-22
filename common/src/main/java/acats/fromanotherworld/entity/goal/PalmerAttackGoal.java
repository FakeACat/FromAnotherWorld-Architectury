package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.resultant.PalmerThingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

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
        if (target != null && this.leapCooldown <= 0 && this.mob.squaredDistanceTo(target) < 100 && !this.mob.targetGrabbed()){
            Vec3d vel = target.getPos().add(0, target.getHeight() / 2, 0).subtract(mob.getPos()).normalize().multiply(2.0F).add(0, 0.2F, 0);
            mob.setVelocity(vel);
            this.leapCooldown = 100;
        }
    }
}
