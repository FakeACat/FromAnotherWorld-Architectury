package mod.acats.fromanotherworld;

import mod.acats.fromanotherlibrary.registry.client.BlockEntityRendererEntry;
import mod.acats.fromanotherlibrary.registry.client.ClientMod;
import mod.acats.fromanotherlibrary.registry.client.EntityRendererEntry;
import mod.acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import mod.acats.fromanotherworld.block.entity.render.TunnelBlockEntityRenderer;
import mod.acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import mod.acats.fromanotherworld.entity.model.thing.revealed.SpiderLegsModel;
import mod.acats.fromanotherworld.entity.render.misc.StarshipRenderer;
import mod.acats.fromanotherworld.entity.render.projectile.NeedleEntityRenderer;
import mod.acats.fromanotherworld.entity.render.thing.TransitionEntityRenderer;
import mod.acats.fromanotherworld.entity.render.thing.resultant.*;
import mod.acats.fromanotherworld.entity.render.thing.revealed.ChestSpitterRenderer;
import mod.acats.fromanotherworld.entity.render.thing.special.AlienThingRenderer;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static mod.acats.fromanotherworld.registry.EntityRegistry.*;

public class FromAnotherWorldClient implements ClientMod {
    @Override
    public Optional<Collection<BlockEntityRendererEntry<?>>> getBlockEntityRendererEntries() {
        return Optional.of(List.of(
                new BlockEntityRendererEntry<>(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), p -> new CorpseBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), p -> new TunnelBlockEntityRenderer())
        ));
    }

    @Override
    public Optional<Collection<EntityRendererEntry<?>>> getEntityRendererEntries() {
        return Optional.of(List.of(
                new EntityRendererEntry<>(CRAWLER.get(), CrawlerRenderer::new),
                new EntityRendererEntry<>(CHEST_SPITTER.get(), ChestSpitterRenderer::new),
                new EntityRendererEntry<>(ASSIMILATION_LIQUID.get(), ThrownItemRenderer::new),
                new EntityRendererEntry<>(JULIETTE_THING.get(), JulietteThingRenderer::new),
                new EntityRendererEntry<>(DOGBEAST.get(), DogBeastRenderer::new),
                new EntityRendererEntry<>(BLOOD_CRAWLER.get(), BloodCrawlerRenderer::new),
                new EntityRendererEntry<>(DOGBEAST_SPITTER.get(), DogBeastSpitterRenderer::new),
                new EntityRendererEntry<>(IMPALER.get(), ImpalerRenderer::new),
                new EntityRendererEntry<>(NEEDLE.get(), NeedleEntityRenderer::new),
                new EntityRendererEntry<>(STARSHIP.get(), StarshipRenderer::new),
                new EntityRendererEntry<>(BEAST.get(), BeastRenderer::new),
                new EntityRendererEntry<>(PALMER_THING.get(), PalmerThingRenderer::new),
                new EntityRendererEntry<>(BLAIR_THING.get(), BlairThingRenderer::new),
                new EntityRendererEntry<>(ALIEN_THING.get(), AlienThingRenderer::new),
                new EntityRendererEntry<>(SPLIT_FACE.get(), SplitFaceRenderer::new),
                new EntityRendererEntry<>(TRANSITION.get(), TransitionEntityRenderer::new),
                new EntityRendererEntry<>(PROWLER.get(), ProwlerRenderer::new)
        ));
    }

    @Override
    public Optional<HashMap<ModelLayerLocation, Supplier<LayerDefinition>>> getModelLayerRegister() {
        HashMap<ModelLayerLocation, Supplier<LayerDefinition>> map = new HashMap<>();

        map.put(SPIDER_LEGS_MODEL_LAYER, SpiderLegsModel::getTexturedModelData);
        map.put(TENTACLE_SEGMENT_MODEL_LAYER, TentacleSegmentModel::createBodyLayer);

        return Optional.of(map);
    }
}
