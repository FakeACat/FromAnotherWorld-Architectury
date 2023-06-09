package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.DogBeastEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DogBeastEntityModel extends GeoModel<DogBeastEntity> {
    @Override
    public ResourceLocation getModelResource(DogBeastEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/dogbeast.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DogBeastEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/dogbeast.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DogBeastEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/dogbeast.animation.json");
    }

    @Override
    public RenderType getRenderType(DogBeastEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
