package acats.fromanotherworld.fabric;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.fabric.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FromAnotherWorldFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FromAnotherWorld.init();

        Config.load(FabricLoader.getInstance().getConfigDir());

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
    }
}
