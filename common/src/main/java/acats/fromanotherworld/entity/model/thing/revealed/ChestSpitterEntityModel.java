package acats.fromanotherworld.entity.model.thing.revealed;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ChestSpitterEntityModel extends GeoModel<ChestSpitterEntity> {
    @Override
    public ResourceLocation getModelResource(ChestSpitterEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/revealed/chest_spitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChestSpitterEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/revealed/chest_spitter/chest_spitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChestSpitterEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/revealed/chest_spitter.animation.json");
    }

    @Override
    public RenderType getRenderType(ChestSpitterEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
