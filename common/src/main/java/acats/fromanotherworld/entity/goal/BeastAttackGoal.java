package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.resultant.BeastEntity;

public class BeastAttackGoal extends ThingAttackGoal {
    public BeastAttackGoal(BeastEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Override
    public boolean canContinueToUse() {
        if (!((BeastEntity) mob).isMelee())
            return false;
        return super.canContinueToUse();
    }

    @Override
    public boolean canUse() {
        if (!((BeastEntity) mob).isMelee())
            return false;
        return super.canUse();
    }
}
