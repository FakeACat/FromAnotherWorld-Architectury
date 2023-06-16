package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.ThingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LeapAttackGoal extends ThingAttackGoal {
    protected final ThingEntity mob;
    private final int leapCooldown;
    private final double leapSpeed;
    private final double leapVerticalBonus;
    private final double leapRangeSquared;
    public LeapAttackGoal(ThingEntity mob, double speed, boolean pauseWhenMobIdle, int leapCooldown, double leapSpeed, double leapVerticalBonus, double leapRange) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
        this.leapCooldown = leapCooldown;
        this.leapSpeed = leapSpeed;
        this.leapVerticalBonus = leapVerticalBonus;
        this.leapRangeSquared = leapRange * leapRange;
    }

    int leapTimer;

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.mob.getTarget();
        this.leapTimer--;
        if (target != null && this.leapTimer < 0 && this.mob.distanceToSqr(target) < leapRangeSquared){
            Vec3 vel = target.position().add(0, target.getBbHeight() / 2, 0).subtract(mob.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.leapSpeed).add(0.0D, this.leapVerticalBonus, 0.0D);
            mob.setDeltaMovement(vel);
            this.leapTimer = this.leapCooldown;
        }
        if (this.mob.canGrief && this.leapTimer > this.leapCooldown - 20){
            this.mob.grief(0, 2);
        }
    }
}
