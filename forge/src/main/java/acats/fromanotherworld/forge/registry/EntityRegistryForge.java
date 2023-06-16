package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import acats.fromanotherworld.entity.model.thing.revealed.SpiderLegsModel;
import acats.fromanotherworld.entity.render.misc.StarshipRenderer;
import acats.fromanotherworld.entity.render.projectile.NeedleEntityRenderer;
import acats.fromanotherworld.entity.render.thing.TransitionEntityRenderer;
import acats.fromanotherworld.entity.render.thing.resultant.*;
import acats.fromanotherworld.entity.render.thing.revealed.ChestSpitterRenderer;
import acats.fromanotherworld.entity.render.thing.special.AlienThingRenderer;
import acats.fromanotherworld.registry.EntityRegistry.FAWEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static acats.fromanotherworld.registry.EntityRegistry.*;

public class EntityRegistryForge {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FromAnotherWorld.MOD_ID);

    public static void register(IEventBus eventBus){
        ENTITY_REGISTRY.forEach(EntityRegistryForge::registerEntity);
        ENTITIES.register(eventBus);
    }

    private static void registerEntity(String id, FAWEntity<?> fawEntity){
        ENTITIES.register(id, () -> fawEntity.build(id));
    }

    public static void clientRegister(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(CRAWLER.get(), CrawlerRenderer::new);
        event.registerEntityRenderer(CHEST_SPITTER.get(), ChestSpitterRenderer::new);
        event.registerEntityRenderer(ASSIMILATION_LIQUID.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(JULIETTE_THING.get(), JulietteThingRenderer::new);
        event.registerEntityRenderer(DOGBEAST.get(), DogBeastRenderer::new);
        event.registerEntityRenderer(BLOOD_CRAWLER.get(), BloodCrawlerRenderer::new);
        event.registerEntityRenderer(DOGBEAST_SPITTER.get(), DogBeastSpitterRenderer::new);
        event.registerEntityRenderer(IMPALER.get(), ImpalerRenderer::new);
        event.registerEntityRenderer(NEEDLE.get(), NeedleEntityRenderer::new);
        event.registerEntityRenderer(STARSHIP.get(), StarshipRenderer::new);
        event.registerEntityRenderer(BEAST.get(), BeastRenderer::new);
        event.registerEntityRenderer(PALMER_THING.get(), PalmerThingRenderer::new);
        event.registerEntityRenderer(BLAIR_THING.get(), BlairThingRenderer::new);
        event.registerEntityRenderer(ALIEN_THING.get(), AlienThingRenderer::new);
        event.registerEntityRenderer(SPLIT_FACE.get(), SplitFaceRenderer::new);
        event.registerEntityRenderer(TRANSITION.get(), TransitionEntityRenderer::new);
    }

    public static void clientRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        spiderLegsModelLayer = new ModelLayerLocation(new ResourceLocation(FromAnotherWorld.MOD_ID, "spider_legs"), "main");
        event.registerLayerDefinition(spiderLegsModelLayer, SpiderLegsModel::getTexturedModelData);
        tentacleSegmentModelLayer = new ModelLayerLocation(new ResourceLocation(FromAnotherWorld.MOD_ID, "tentacle_segment"), "main");
        event.registerLayerDefinition(tentacleSegmentModelLayer, TentacleSegmentModel::createBodyLayer);
    }
}
