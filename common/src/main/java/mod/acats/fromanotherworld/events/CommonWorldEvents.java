package mod.acats.fromanotherworld.events;

import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.memory.GlobalThingMemory;
import mod.acats.fromanotherworld.spawning.SpawningManager;
import mod.acats.fromanotherworld.utilities.chunkloading.FAWChunkLoaders;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;

public class CommonWorldEvents {
    public static void serverWorldTick(ServerLevel world){
        if (world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT) && world.getDayTime() % 24000 == 6000 && Config.EVENT_CONFIG.enabled.get()){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.update(world);
        }

        if (world.getRandom().nextInt(300) == 0){
            SpawningManager spawningManager = SpawningManager.getSpawningManager(world);
            spawningManager.alienThingSpawner(world);
        }

        if (world.getGameTime() % 1200 == 0){
            FAWChunkLoaders.getChunkLoaders(world).tick(world);
            GlobalThingMemory.getGlobalThingMemory(world).tick();
        }
    }
}
