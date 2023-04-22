package acats.fromanotherworld.fabric;

import acats.fromanotherworld.FromAnotherWorldClient;
import acats.fromanotherworld.fabric.registry.BlockRegistryFabric;
import acats.fromanotherworld.fabric.registry.EntityRegistryFabric;
import acats.fromanotherworld.fabric.registry.ParticleRegistryFabric;
import net.fabricmc.api.ClientModInitializer;

public class FromAnotherWorldClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FromAnotherWorldClient.initClient();

        EntityRegistryFabric.clientRegister();
        BlockRegistryFabric.clientRegister();
        ParticleRegistryFabric.clientRegister();
    }
}
