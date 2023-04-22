package acats.fromanotherworld.spawning;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class StarshipEvent extends AbstractThingEvent{
    public StarshipEvent(ServerWorld world, ServerPlayerEntity player) {
        super(world, player);
    }

    @Override
    public void run() {
        StarshipEntity ship = EntityRegistry.STARSHIP.get().create(world);
        if (ship != null){
            ship.setPosition(player.getX() - 128 + player.getRandom().nextInt(256), 500, player.getZ() - 128 + player.getRandom().nextInt(256));
            world.spawnEntity(ship);
        }
        super.run();
    }

    @Override
    String warning() {
        return "Something is falling from the sky...";
    }
}
