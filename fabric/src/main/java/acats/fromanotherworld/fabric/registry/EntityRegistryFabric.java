package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.revealed.SpiderLegsEntityModel;
import acats.fromanotherworld.entity.render.misc.StarshipEntityRenderer;
import acats.fromanotherworld.entity.render.projectile.NeedleEntityRenderer;
import acats.fromanotherworld.entity.render.resultant.*;
import acats.fromanotherworld.entity.render.revealed.ChestSpitterEntityRenderer;
import acats.fromanotherworld.entity.render.special.AlienThingEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static acats.fromanotherworld.registry.EntityRegistry.*;

public class EntityRegistryFabric {
    public static void register(){
        /*for (LivingEntityToRegister<?> livingEntityToRegister:
                CRINGE_MULTILOADER_LIVING_ENTITY_REGISTRY) {
            Registry.register(Registries.ENTITY_TYPE, livingEntityToRegister.identifier, livingEntityToRegister.build());
            FabricDefaultAttributeRegistry.register(livingEntityToRegister.get(), livingEntityToRegister.attributeSupplier.get());
        }
        for (EntityToRegister<?> entityToRegister:
                CRINGE_MULTILOADER_PROJECTILE_REGISTRY){
            Registry.register(Registries.ENTITY_TYPE, entityToRegister.identifier, entityToRegister.build());
        }*/
        ENTITY_REGISTRY.forEach(EntityRegistryFabric::registerEntity);
        ATTRIBUTE_REGISTRY.forEach(EntityRegistryFabric::registerAttributes);
    }

    private static void registerEntity(String id, FAWEntity<?> fawEntity){
        Registry.register(Registries.ENTITY_TYPE, new Identifier(FromAnotherWorld.MOD_ID, id), fawEntity.build(id));
    }
    private static void registerAttributes(String id, Supplier<DefaultAttributeContainer.Builder> attributeSupplier){
        FabricDefaultAttributeRegistry.register(LIVING_ENTITY_REGISTRY.get(id).get(), attributeSupplier.get());
    }

    public static void clientRegister(){
        spiderLegsModelLayer = new EntityModelLayer(new Identifier(FromAnotherWorld.MOD_ID, "spider_legs"), "main");
        EntityModelLayerRegistry.registerModelLayer(spiderLegsModelLayer, SpiderLegsEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(CRAWLER.get(), CrawlerEntityRenderer::new);
        EntityRendererRegistry.register(CHEST_SPITTER.get(), ChestSpitterEntityRenderer::new);
        EntityRendererRegistry.register(ASSIMILATION_LIQUID.get(), FlyingItemEntityRenderer::new);
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
    }
}
