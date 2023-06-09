package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.DogBeastSpitterEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DogBeastSpitterEntityModel extends GeoModel<DogBeastSpitterEntity> {
    @Override
    public ResourceLocation getModelResource(DogBeastSpitterEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/dogbeast_spitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DogBeastSpitterEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/dogbeast_spitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DogBeastSpitterEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/dogbeast_spitter.animation.json");
    }

    @Override
    public RenderType getRenderType(DogBeastSpitterEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
