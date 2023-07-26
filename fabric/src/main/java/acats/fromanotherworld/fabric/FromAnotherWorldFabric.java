package acats.fromanotherworld.fabric;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.fabric.registry.*;
import acats.fromanotherworld.utilities.ModLoaderDependant;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FromAnotherWorldFabric implements ModInitializer {

    private static final ModLoaderDependant FABRIC_DEPENDANT = () -> FabricLoader.getInstance().isDevelopmentEnvironment();

    @Override
    public void onInitialize() {
        Config.load(FabricLoader.getInstance().getConfigDir());

        FromAnotherWorld.init();
        FromAnotherWorld.mlDep = FABRIC_DEPENDANT;

        EntityRegistryFabric.register();
        ItemRegistryFabric.register();
        BlockRegistryFabric.register();
        ParticleRegistryFabric.register();
        SoundRegistryFabric.register();
        StatusEffectRegistryFabric.register();
        EventRegistryFabric.register();
        DatapackRegistryFabric.register();
        RecipeRegistryFabric.register();
        BlockEntityRegistryFabric.register();
        SpawnEntryRegistryFabric.register();
    }
}
