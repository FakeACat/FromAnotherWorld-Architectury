package mod.acats.fromanotherworld.tags;

import mod.acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeTags {
    public static final TagKey<Biome> SPAWNS_THINGS = biomeTag("spawns_things");
    private static TagKey<Biome> biomeTag(String id){
        return TagKey.create(Registries.BIOME, new ResourceLocation(FromAnotherWorld.MOD_ID, id));
    }
}
