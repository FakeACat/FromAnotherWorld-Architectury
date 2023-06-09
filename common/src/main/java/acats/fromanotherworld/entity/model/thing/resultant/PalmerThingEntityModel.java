package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class PalmerThingEntityModel extends GeoModel<PalmerThingEntity> {
    @Override
    public ResourceLocation getModelResource(PalmerThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/palmer_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PalmerThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PalmerThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/palmer_thing.animation.json");
    }
}
