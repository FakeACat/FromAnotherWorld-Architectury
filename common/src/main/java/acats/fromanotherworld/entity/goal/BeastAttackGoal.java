package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.resultant.Beast;

public class BeastAttackGoal extends ThingAttackGoal {
    public BeastAttackGoal(Beast mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Override
    public boolean canContinueToUse() {
        if (!((Beast) mob).isMelee())
            return false;
        return super.canContinueToUse();
    }

    @Override
    public boolean canUse() {
        if (!((Beast) mob).isMelee())
            return false;
        return super.canUse();
    }
}
