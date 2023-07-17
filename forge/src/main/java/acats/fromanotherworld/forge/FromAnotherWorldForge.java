package acats.fromanotherworld.forge;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.forge.biomemodifiers.FAWBiomeModifier;
import acats.fromanotherworld.forge.registry.*;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(FromAnotherWorld.MOD_ID)
public class FromAnotherWorldForge {
    public FromAnotherWorldForge() {
        Config.load(FMLPaths.CONFIGDIR.get());

        FromAnotherWorld.init();

        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(eventBus);

        EntityRegistryForge.register(eventBus);
        ItemRegistryForge.register(eventBus);
        BlockRegistryForge.register(eventBus);
        ParticleRegistryForge.register(eventBus);
        SoundRegistryForge.register(eventBus);
        StatusEffectRegistryForge.register(eventBus);
        RecipeRegistryForge.register(eventBus);
        BlockEntityRegistryForge.register(eventBus);
        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FromAnotherWorld.MOD_ID);
        biomeModifiers.register(eventBus);
        biomeModifiers.register("thing_spawns", FAWBiomeModifier::makeCodec);

        eventBus.addListener(DatapackRegistryForge::register);
    }
}
