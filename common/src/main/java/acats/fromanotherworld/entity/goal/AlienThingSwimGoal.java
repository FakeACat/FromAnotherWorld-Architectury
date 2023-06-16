package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.special.AlienThing;

public class AlienThingSwimGoal extends ImprovedSwimGoal {
    AlienThing alien;
    public AlienThingSwimGoal(AlienThing mob, float speed) {
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
