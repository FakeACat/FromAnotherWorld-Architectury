package acats.fromanotherworld.spawning;

import acats.fromanotherworld.entity.DisguisedThing;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class DisguisedDogEvent extends AbstractThingMobEvent {
    public DisguisedDogEvent(ServerWorld world, ServerPlayerEntity player) {
        super(world, player);
    }

    @Override
    void setMobs() {
        WolfEntity wolfEntity = EntityType.WOLF.create(world);
        if (wolfEntity != null){
            ((DisguisedThing) wolfEntity).setAssimilated();
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
