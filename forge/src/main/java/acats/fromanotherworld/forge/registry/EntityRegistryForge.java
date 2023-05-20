package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.thing.revealed.SpiderLegsEntityModel;
import acats.fromanotherworld.entity.render.misc.StarshipEntityRenderer;
import acats.fromanotherworld.entity.render.projectile.NeedleEntityRenderer;
import acats.fromanotherworld.entity.render.thing.resultant.*;
import acats.fromanotherworld.entity.render.thing.revealed.ChestSpitterEntityRenderer;
import acats.fromanotherworld.entity.render.thing.special.AlienThingEntityRenderer;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
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
        event.registerEntityRenderer(CRAWLER.get(), CrawlerEntityRenderer::new);
        event.registerEntityRenderer(CHEST_SPITTER.get(), ChestSpitterEntityRenderer::new);
        event.registerEntityRenderer(ASSIMILATION_LIQUID.get(), FlyingItemEntityRenderer::new);
        event.registerEntityRenderer(JULIETTE_THING.get(), JulietteThingEntityRenderer::new);
        event.registerEntityRenderer(DOGBEAST.get(), DogBeastEntityRenderer::new);
        event.registerEntityRenderer(BLOOD_CRAWLER.get(), BloodCrawlerEntityRenderer::new);
        event.registerEntityRenderer(DOGBEAST_SPITTER.get(), DogBeastSpitterEntityRenderer::new);
        event.registerEntityRenderer(IMPALER.get(), ImpalerEntityRenderer::new);
        event.registerEntityRenderer(NEEDLE.get(), NeedleEntityRenderer::new);
        event.registerEntityRenderer(STARSHIP.get(), StarshipEntityRenderer::new);
        event.registerEntityRenderer(BEAST.get(), BeastEntityRenderer::new);
        event.registerEntityRenderer(PALMER_THING.get(), PalmerThingEntityRenderer::new);
        event.registerEntityRenderer(BLAIR_THING.get(), BlairThingEntityRenderer::new);
        event.registerEntityRenderer(ALIEN_THING.get(), AlienThingEntityRenderer::new);
        event.registerEntityRenderer(SPLIT_FACE.get(), SplitFaceEntityRenderer::new);
    }

    public static void clientRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        spiderLegsModelLayer = new EntityModelLayer(new Identifier(FromAnotherWorld.MOD_ID, "spider_legs"), "main");
        event.registerLayerDefinition(EntityRegistry.spiderLegsModelLayer, SpiderLegsEntityModel::getTexturedModelData);
    }
}
