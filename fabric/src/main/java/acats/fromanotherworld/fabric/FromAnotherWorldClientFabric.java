package acats.fromanotherworld.fabric;

import acats.fromanotherworld.fabric.registry.BlockEntityRegistryFabric;
import acats.fromanotherworld.fabric.registry.BlockRegistryFabric;
import acats.fromanotherworld.fabric.registry.EntityRegistryFabric;
import acats.fromanotherworld.fabric.registry.ParticleRegistryFabric;
import net.fabricmc.api.ClientModInitializer;

public class FromAnotherWorldClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRegistryFabric.clientRegister();
        ParticleRegistryFabric.clientRegister();
        BlockRegistryFabric.registerClient();
        BlockEntityRegistryFabric.clientRegister();
    }
}
