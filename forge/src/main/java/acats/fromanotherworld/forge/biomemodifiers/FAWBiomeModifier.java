package acats.fromanotherworld.forge.biomemodifiers;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.SpawnEntryRegistry;
import acats.fromanotherworld.tags.BiomeTags;
import acats.fromanotherworld.utilities.registry.FAWSpawnEntry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FAWBiomeModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER =
            RegistryObject.create(new ResourceLocation(FromAnotherWorld.MOD_ID, "thing_spawns"),
                    ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FromAnotherWorld.MOD_ID);

    @Override
    public void modify(Holder<Biome> arg, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (arg.is(BiomeTags.SPAWNS_THINGS)){
            for (FAWSpawnEntry<?> e: SpawnEntryRegistry.entries) {
                builder.getMobSpawnSettings().getSpawner(e.category()).add(new MobSpawnSettings.SpawnerData(e.entityTypeSupplier().get(), e.weight(), e.groupMin(), e.groupMax()));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<FAWBiomeModifier> makeCodec() {
        return Codec.unit(FAWBiomeModifier::new);
    }
}
