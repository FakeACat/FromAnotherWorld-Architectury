package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.interfaces.Leaper;
import acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LeapAttackGoal extends ThingAttackGoal {
    protected final Thing mob;
    private final int leapCooldown;
    private final double leapSpeed;
    private final double leapVerticalBonus;
    private final double leapRangeSquared;
    private final Leaper leaper;
    public LeapAttackGoal(Thing mob, double speed, boolean pauseWhenMobIdle, int leapCooldown, double leapSpeed, double leapVerticalBonus, double leapRange) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
        this.leapCooldown = leapCooldown;
        this.leapSpeed = leapSpeed;
        this.leapVerticalBonus = leapVerticalBonus;
        this.leapRangeSquared = leapRange * leapRange;
        this.leaper = mob instanceof Leaper leaper1 ? leaper1 : null;
    }

    int leapTimer;

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.mob.getTarget();
        this.leapTimer--;
        if(target != null){
            if (this.leapTimer < 0 && this.mob.distanceToSqr(target) < leapRangeSquared){
                Vec3 vel = target.position().add(0, target.getBbHeight() / 2, 0).subtract(mob.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.leapSpeed).add(0.0D, this.leapVerticalBonus, 0.0D);
                mob.setDeltaMovement(vel);
                this.leapTimer = this.leapCooldown;
                if (this.leaper != null) {
                    this.leaper.setLeaping(true);
                }
            }
            if (this.leapTimer > this.leapCooldown - 20){
                if(this.mob.canGrief){
                    this.mob.grief(0, 2);
                }

                double e = target.getX() - this.mob.getX();
                double f = target.getZ() - this.mob.getZ();
                this.mob.setYRot(-((float) Mth.atan2(e, f)) * 57.295776F);
                this.mob.yBodyRot = this.mob.getYRot();
            }
        }
        if (this.leapTimer < this.leapCooldown - 5 && this.leaper != null && this.mob.onGround()){
            this.leaper.setLeaping(false);
        }
    }

    @Override
    public void stop() {
        if (this.leaper != null){
            this.leaper.setLeaping(false);
        }
        super.stop();
    }
}
