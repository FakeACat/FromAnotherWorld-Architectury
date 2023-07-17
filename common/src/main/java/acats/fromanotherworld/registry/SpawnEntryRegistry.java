package acats.fromanotherworld.registry;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.config.SpawningConfig;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.tags.BiomeTags;
import acats.fromanotherworld.utilities.registry.FAWSpawnEntry;
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
        SpawningConfig config = Config.spawningConfig;
        if (config.enabled.get()){
            for (SpawningConfig.Entry<?> entry:
                 config.entries) {
                if (entry.enabled.get() && entry.weight.get() > 0 && entry.max.get() > 0){
                    addThing(entry.supplier, entry.weight.get(), entry.min.get(), entry.max.get());
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
