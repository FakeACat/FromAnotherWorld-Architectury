package acats.fromanotherworld.forge;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.forge.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(FromAnotherWorld.MOD_ID)
public class FromAnotherWorldForge {
    public FromAnotherWorldForge() {
        FromAnotherWorld.init();

        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(eventBus);

        EntityRegistryForge.register(eventBus);
        ItemRegistryForge.register(eventBus);
        BlockRegistryForge.register(eventBus);
        ParticleRegistryForge.register(eventBus);
        SoundRegistryForge.register(eventBus);
        StatusEffectRegistryForge.register(eventBus);

        Config.load(FMLPaths.CONFIGDIR.get());

        eventBus.addListener(DatapackRegistryForge::register);
    }
}
