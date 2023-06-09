package acats.fromanotherworld.spawning;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class StarshipEvent extends AbstractThingEvent{
    public StarshipEvent(ServerLevel world, ServerPlayer player) {
        super(world, player);
    }

    @Override
    public void run() {
        StarshipEntity ship = EntityRegistry.STARSHIP.get().create(world);
        if (ship != null){
            ship.setPos(player.getX() - 128 + player.getRandom().nextInt(256), 500, player.getZ() - 128 + player.getRandom().nextInt(256));
            world.addFreshEntity(ship);
        }
        super.run();
    }

    @Override
    String warning() {
        return "Something is falling from the sky...";
    }
}
