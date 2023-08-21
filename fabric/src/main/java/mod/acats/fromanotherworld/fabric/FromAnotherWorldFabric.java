package mod.acats.fromanotherworld.fabric;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.fabric.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FromAnotherWorldFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Config.load(FabricLoader.getInstance().getConfigDir());

        new FromAnotherWorld().init();
        ParticleRegistryFabric.register();
        SoundRegistryFabric.register();
        StatusEffectRegistryFabric.register();
        EventRegistryFabric.register();
        DatapackRegistryFabric.register();
        RecipeRegistryFabric.register();
        SpawnEntryRegistryFabric.register();
    }
}
