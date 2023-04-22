package acats.fromanotherworld.events;

import acats.fromanotherworld.config.General;
import acats.fromanotherworld.spawning.SpawningManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public class CommonWorldEvents {
    public static void serverWorldTick(ServerWorld world){
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE) && world.getTimeOfDay() % 24000 == 6000 && General.thingEventsEnabled){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.update(world);
        }

        if (world.getRandom().nextInt(2400) == 0){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.alienThingSpawner(world);
        }
    }
}
