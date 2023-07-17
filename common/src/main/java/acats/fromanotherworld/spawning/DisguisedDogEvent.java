package acats.fromanotherworld.spawning;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;

public class DisguisedDogEvent extends AbstractThingMobEvent {
    public DisguisedDogEvent(ServerLevel world, ServerPlayer player) {
        super(world, player);
    }

    @Override
    void setMobs() {
        Wolf wolfEntity = EntityType.WOLF.create(world);
        if (wolfEntity != null){
            ((PossibleDisguisedThing) wolfEntity).faw$setAssimilated(true);
            this.addToSpawns(wolfEntity);
        }
    }

    @Override
    String warning() {
        return "There is an Impostor Among Us";
    }

    @Override
    int range() {
        return 64;
    }
}
