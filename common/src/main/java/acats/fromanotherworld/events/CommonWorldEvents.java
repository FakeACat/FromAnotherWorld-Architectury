package acats.fromanotherworld.events;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.spawning.SpawningManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;

public class CommonWorldEvents {
    public static void serverWorldTick(ServerLevel world){
        if (world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT) && world.getDayTime() % 24000 == 6000 && Config.eventConfig.enabled.get()){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.update(world);
        }

        if (world.getRandom().nextInt(2400) == 0){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.alienThingSpawner(world);
        }
    }
}
