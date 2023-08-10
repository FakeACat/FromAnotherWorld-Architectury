package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.entity.thing.resultant.BlairThing;
import net.minecraft.world.entity.LivingEntity;

public class BlairThingAttackGoal extends ThingAttackGoal{
    BlairThing blairThing;
    public BlairThingAttackGoal(BlairThing mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.blairThing = mob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
        if (this.blairThing.getMoveCooldown() >= BlairThing.MOVE_COOLDOWN_IN_TICKS - (BlairThing.EMERGE_TIME_IN_TICKS + BlairThing.TIME_UNDERGROUND_IN_TICKS))
            return;
        super.checkAndPerformAttack(target, squaredDistance);
    }
}
