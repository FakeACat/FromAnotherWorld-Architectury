package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.PalmerThing;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class PalmerThingModel extends GeoModel<PalmerThing> {
    @Override
    public ResourceLocation getModelResource(PalmerThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/palmer_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PalmerThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PalmerThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/palmer_thing.animation.json");
    }
}
