package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.entity.interfaces.BurstAttackThing;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class ThingProjectileBurstGoal extends Goal {
    private final BurstAttackThing thing;
    private final Thing mob;
    private final float range;
    private final int chargeUpTime;
    private int cooldown;
    public ThingProjectileBurstGoal(BurstAttackThing thing, float range, int chargeUpTime){
        this.thing = thing;
        this.mob = (Thing)thing;
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
