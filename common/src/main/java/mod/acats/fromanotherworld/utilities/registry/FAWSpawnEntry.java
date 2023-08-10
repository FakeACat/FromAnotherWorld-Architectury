package mod.acats.fromanotherworld.utilities.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public record FAWSpawnEntry<T extends Mob>(Supplier<EntityType<T>> entityTypeSupplier,
                                           MobCategory category,
                                           int weight,
                                           int groupMin,
                                           int groupMax,
                                           SpawnPlacements.Type placementType,
                                           Heightmap.Types heightmapType,
                                           SpawnPlacements.SpawnPredicate<T> spawnPredicate,
                                           TagKey<Biome> biomeTag) {

}
