package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.special.AlienThingEntity;

public class AlienThingSwimGoal extends ImprovedSwimGoal {
    AlienThingEntity alien;
    public AlienThingSwimGoal(AlienThingEntity mob, float speed) {
        super(mob, speed);
        this.alien = mob;
    }

    @Override
    public void tick() {
        if (this.alien.bored){
            this.alien.escape();
        }
        else{
            super.tick();
        }
    }
}
