package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.BlairThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BlairThingEntityModel extends GeoModel<BlairThingEntity> {
    @Override
    public ResourceLocation getModelResource(BlairThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/blair_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlairThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/blair_thing.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlairThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/blair_thing.animation.json");
    }

    @Override
    public RenderType getRenderType(BlairThingEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
