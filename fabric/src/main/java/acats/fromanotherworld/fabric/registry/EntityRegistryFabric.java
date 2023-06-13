package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import acats.fromanotherworld.entity.model.thing.revealed.SpiderLegsEntityModel;
import acats.fromanotherworld.entity.render.misc.StarshipEntityRenderer;
import acats.fromanotherworld.entity.render.projectile.NeedleEntityRenderer;
import acats.fromanotherworld.entity.render.thing.TransitionEntityRenderer;
import acats.fromanotherworld.entity.render.thing.resultant.*;
import acats.fromanotherworld.entity.render.thing.revealed.ChestSpitterEntityRenderer;
import acats.fromanotherworld.entity.render.thing.special.AlienThingEntityRenderer;
import acats.fromanotherworld.registry.EntityRegistry.FAWEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import java.util.function.Supplier;

import static acats.fromanotherworld.registry.EntityRegistry.*;

public class EntityRegistryFabric {
    public static void register(){
        ENTITY_REGISTRY.forEach(EntityRegistryFabric::registerEntity);
        ATTRIBUTE_REGISTRY.forEach(EntityRegistryFabric::registerAttributes);
    }

    private static void registerEntity(String id, FAWEntity<?> fawEntity){
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, id), fawEntity.build(id));
    }
    private static void registerAttributes(String id, Supplier<AttributeSupplier.Builder> attributeSupplier){
        FabricDefaultAttributeRegistry.register(LIVING_ENTITY_REGISTRY.get(id).get(), attributeSupplier.get());
    }

    public static void clientRegister(){
        spiderLegsModelLayer = new ModelLayerLocation(new ResourceLocation(FromAnotherWorld.MOD_ID, "spider_legs"), "main");
        EntityModelLayerRegistry.registerModelLayer(spiderLegsModelLayer, SpiderLegsEntityModel::getTexturedModelData);
        tentacleSegmentModelLayer = new ModelLayerLocation(new ResourceLocation(FromAnotherWorld.MOD_ID, "tentacle_segment"), "main");
        EntityModelLayerRegistry.registerModelLayer(tentacleSegmentModelLayer, TentacleSegmentModel::createBodyLayer);

        EntityRendererRegistry.register(CRAWLER.get(), CrawlerEntityRenderer::new);
        EntityRendererRegistry.register(CHEST_SPITTER.get(), ChestSpitterEntityRenderer::new);
        EntityRendererRegistry.register(ASSIMILATION_LIQUID.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(JULIETTE_THING.get(), JulietteThingEntityRenderer::new);
        EntityRendererRegistry.register(DOGBEAST.get(), DogBeastEntityRenderer::new);
        EntityRendererRegistry.register(BLOOD_CRAWLER.get(), BloodCrawlerEntityRenderer::new);
        EntityRendererRegistry.register(DOGBEAST_SPITTER.get(), DogBeastSpitterEntityRenderer::new);
        EntityRendererRegistry.register(IMPALER.get(), ImpalerEntityRenderer::new);
        EntityRendererRegistry.register(NEEDLE.get(), NeedleEntityRenderer::new);
        EntityRendererRegistry.register(STARSHIP.get(), StarshipEntityRenderer::new);
        EntityRendererRegistry.register(BEAST.get(), BeastEntityRenderer::new);
        EntityRendererRegistry.register(PALMER_THING.get(), PalmerThingEntityRenderer::new);
        EntityRendererRegistry.register(BLAIR_THING.get(), BlairThingEntityRenderer::new);
        EntityRendererRegistry.register(ALIEN_THING.get(), AlienThingEntityRenderer::new);
        EntityRendererRegistry.register(SPLIT_FACE.get(), SplitFaceEntityRenderer::new);
        EntityRendererRegistry.register(TRANSITION.get(), TransitionEntityRenderer::new);
    }
}
