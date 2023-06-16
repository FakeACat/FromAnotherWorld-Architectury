package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface StalkerThing {

    int HUNTING_RANGE_SQUARED = Thing.HUNTING_RANGE * Thing.HUNTING_RANGE;

    Player getStalkTarget();

    void setStalkTarget(Player stalkTarget);

    default Player findStalkTarget(){
        if (this instanceof Entity entity){

            if (this.isAcceptableStalkTarget(this.getStalkTarget()))
                return this.getStalkTarget();

            Player stalkTarget = entity.level().getNearestPlayer(entity, Thing.HUNTING_RANGE);

            if (this.isAcceptableStalkTarget(stalkTarget)) {
                this.setStalkTarget(stalkTarget);
                return stalkTarget;
            }
        }

        return null;
    }

    default boolean isAcceptableStalkTarget(Player playerEntity){
        return playerEntity != null && !playerEntity.isCreative() && !playerEntity.isSpectator() && playerEntity.distanceToSqr((Entity) this) < HUNTING_RANGE_SQUARED;
    }
}
