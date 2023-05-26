package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.ThingEntity;
import acats.fromanotherworld.entity.interfaces.BurstAttackThing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

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
    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.thing.canShootBurst() && this.mob.squaredDistanceTo(livingEntity) < this.range * this.range;
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
