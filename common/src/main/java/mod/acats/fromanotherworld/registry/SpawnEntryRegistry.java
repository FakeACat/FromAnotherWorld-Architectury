package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.config.v2.SpawnValues;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.config.SpawningConfig;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.tags.BiomeTags;
import mod.acats.fromanotherworld.utilities.registry.FAWSpawnEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SpawnEntryRegistry {
    public static ArrayList<FAWSpawnEntry<?>> entries = new ArrayList<>();
    public static void register(){
        SpawningConfig config = Config.SPAWNING_CONFIG;
        if (config.enabled.get()){
            for (SpawningConfig.SpawnEntry<?> entry:
                 config.spawnEntries) {
                SpawnValues property = entry.values().get();
                if (property.enabled() && property.weight() > 0 && property.groupMin() > 0){
                    addThing(entry.typeSupplier(), property.weight(), property.groupMin(), property.groupMax());
                }
            }
        }
    }

    private static <T extends Monster> void addThing(Supplier<EntityType<T>> entityTypeSupplier, int weight, int min, int max){
        entries.add(new FAWSpawnEntry<>(entityTypeSupplier,
                MobCategory.MONSTER,
                weight, min, max,
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Thing::checkThingSpawnRules, BiomeTags.SPAWNS_THINGS));
    }
}
