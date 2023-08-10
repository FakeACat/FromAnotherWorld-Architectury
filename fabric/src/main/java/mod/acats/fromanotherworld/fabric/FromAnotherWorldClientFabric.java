package mod.acats.fromanotherworld.fabric;

import mod.acats.fromanotherworld.fabric.registry.BlockRegistryFabric;
import mod.acats.fromanotherworld.fabric.registry.EntityRegistryFabric;
import mod.acats.fromanotherworld.fabric.registry.ParticleRegistryFabric;
import mod.acats.fromanotherworld.fabric.registry.client.BlockEntityRegistryClientFabric;
import net.fabricmc.api.ClientModInitializer;

public class FromAnotherWorldClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRegistryFabric.clientRegister();
        ParticleRegistryFabric.clientRegister();
        BlockRegistryFabric.registerClient();
        BlockEntityRegistryClientFabric.clientRegister();
    }
}
