package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.ThingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import acats.fromanotherworld.entity.interfaces.BurstAttackThing;

public class ThingProjectileBurstGoal extends Goal {
    private final BurstAttackThing thing;
    private final ThingEntity mob;
    private final float range;
    private final int chargeUpTime;
    private int cooldown;
    public ThingProjectileBurstGoal(BurstAttackThing thing, float range, int chargeUpTime){
        this.thing = thing;
        this.mob = (ThingEntity)thing;
        this.range = range;
        this.chargeUpTime = chargeUpTime;
    }
    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.mob.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.thing.canShootBurst() && this.mob.distanceToSqr(livingEntity) < this.range * this.range;
    }

    @Override
    public void tick() {
        if (++cooldown >= this.chargeUpTime){
            thing.shootBurst(this.mob.getTarget());
            this.cooldown = 0;
        }
    }

    @Override
    public void stop() {
        this.cooldown = 0;
    }
}
