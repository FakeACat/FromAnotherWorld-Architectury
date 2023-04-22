package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.resultant.BeastEntity;

public class BeastAttackGoal extends ThingAttackGoal {
    public BeastAttackGoal(BeastEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Override
    public boolean shouldContinue() {
        if (!((BeastEntity) mob).isMelee())
            return false;
        return super.shouldContinue();
    }

    @Override
    public boolean canStart() {
        if (!((BeastEntity) mob).isMelee())
            return false;
        return super.canStart();
    }
}
