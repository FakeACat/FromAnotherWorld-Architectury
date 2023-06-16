package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.DogBeastSpitter;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DogBeastSpitterModel extends GeoModel<DogBeastSpitter> {
    @Override
    public ResourceLocation getModelResource(DogBeastSpitter object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/dogbeast_spitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DogBeastSpitter object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/dogbeast_spitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DogBeastSpitter animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/dogbeast_spitter.animation.json");
    }

    @Override
    public RenderType getRenderType(DogBeastSpitter animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
