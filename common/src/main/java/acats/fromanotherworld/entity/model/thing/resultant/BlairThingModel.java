package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.BlairThing;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BlairThingModel extends GeoModel<BlairThing> {
    @Override
    public ResourceLocation getModelResource(BlairThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/blair_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlairThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/blair_thing.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlairThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/blair_thing.animation.json");
    }

    @Override
    public RenderType getRenderType(BlairThing animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
