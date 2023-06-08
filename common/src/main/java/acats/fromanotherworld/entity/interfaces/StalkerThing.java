package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.entity.thing.ThingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public interface StalkerThing {

    int HUNTING_RANGE_SQUARED = ThingEntity.HUNTING_RANGE * ThingEntity.HUNTING_RANGE;

    PlayerEntity getStalkTarget();

    void setStalkTarget(PlayerEntity stalkTarget);

    default PlayerEntity findStalkTarget(){
        if (this instanceof Entity entity){

            if (this.isAcceptableStalkTarget(this.getStalkTarget()))
                return this.getStalkTarget();

            PlayerEntity stalkTarget = entity.getWorld().getClosestPlayer(entity, ThingEntity.HUNTING_RANGE);

            if (this.isAcceptableStalkTarget(stalkTarget)) {
                this.setStalkTarget(stalkTarget);
                return stalkTarget;
            }
        }

        return null;
    }

    default boolean isAcceptableStalkTarget(PlayerEntity playerEntity){
        return playerEntity != null && !playerEntity.isCreative() && !playerEntity.isSpectator() && playerEntity.squaredDistanceTo((Entity) this) < HUNTING_RANGE_SQUARED;
    }
}
