package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.registry.SpawnEntryRegistry;
import acats.fromanotherworld.utilities.registry.FAWSpawnEntry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;

public class SpawnEntryRegistryFabric {
    public static void register(){
        for (FAWSpawnEntry<?> e: SpawnEntryRegistry.entries) {
            add(e);
        }
    }

    private static <T extends Mob> void add(FAWSpawnEntry<T> e){
        BiomeModifications.addSpawn(BiomeSelectors.all().and(ctx -> ctx.hasTag(e.biomeTag())), e.category(), e.entityTypeSupplier().get(), e.weight(), e.groupMin(), e.groupMax());
        SpawnPlacements.register(e.entityTypeSupplier().get(), e.placementType(), e.heightmapType(), e.spawnPredicate());
    }
}
