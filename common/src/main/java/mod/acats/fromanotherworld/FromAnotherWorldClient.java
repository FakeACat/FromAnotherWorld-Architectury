package mod.acats.fromanotherworld;

import mod.acats.fromanotherlibrary.registry.client.BlockEntityRendererEntry;
import mod.acats.fromanotherlibrary.registry.client.ClientMod;
import mod.acats.fromanotherlibrary.registry.client.EntityRendererEntry;
import mod.acats.fromanotherlibrary.registry.client.ParticleClientEntry;
import mod.acats.fromanotherworld.block.entity.render.*;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.registry.client.ClientEntityRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.client.particle.WaterDropParticle;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FromAnotherWorldClient implements ClientMod {
    @Override
    public Optional<Iterable<BlockEntityRendererEntry<?>>> getBlockEntityRendererEntries() {
        return Optional.of(List.of(
                new BlockEntityRendererEntry<>(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), p -> new CorpseBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), p -> new TunnelBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.ASSIMILATED_SCULK_TENTACLES_BLOCK_ENTITY.get(), p -> new AssimilatedSculkTentaclesBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.ASSIMILATED_SCULK_ACTIVATOR_BLOCK_ENTITY.get(), p -> new AssimilatedSculkActivatorBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.ASSIMILATED_SCULK_OVERGROWTH_BLOCK_ENTITY.get(), p -> new AssimilatedSculkOvergrowthBlockEntityRenderer())
        ));
    }

    @Override
    public Optional<Iterable<EntityRendererEntry<?>>> getEntityRendererEntries() {
        return Optional.of(ClientEntityRegistry.registerEntityRenderers());
    }

    @Override
    public Optional<Iterable<ParticleClientEntry<?>>> getParticleClientEntries() {
        return Optional.of(List.of(
                new ParticleClientEntry<>(ParticleRegistry.THING_GORE.get(), WaterDropParticle.Provider::new),
                new ParticleClientEntry<>(ParticleRegistry.THING_SPIT.get(), SpitParticle.Provider::new),
                new ParticleClientEntry<>(ParticleRegistry.BIG_FLAMES.get(), CampfireSmokeParticle.SignalProvider::new)
        ));
    }

    @Override
    public Optional<HashMap<ModelLayerLocation, Supplier<LayerDefinition>>> getModelLayerRegister() {
        return Optional.of(ClientEntityRegistry.registerModelLayers(new HashMap<>()));
    }
}
