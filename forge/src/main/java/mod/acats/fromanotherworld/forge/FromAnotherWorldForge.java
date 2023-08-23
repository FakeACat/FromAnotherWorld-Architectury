package mod.acats.fromanotherworld.forge;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.forge.biomemodifiers.FAWBiomeModifier;
import mod.acats.fromanotherworld.forge.registry.*;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(FromAnotherWorld.MOD_ID)
public class FromAnotherWorldForge {
    public FromAnotherWorldForge() {
        new FromAnotherWorld().init();

        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(eventBus);

        SoundRegistryForge.register(eventBus);
        StatusEffectRegistryForge.register(eventBus);
        RecipeRegistryForge.register(eventBus);
        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FromAnotherWorld.MOD_ID);
        biomeModifiers.register(eventBus);
        biomeModifiers.register("thing_spawns", FAWBiomeModifier::makeCodec);

        eventBus.addListener(DatapackRegistryForge::register);
    }
}
