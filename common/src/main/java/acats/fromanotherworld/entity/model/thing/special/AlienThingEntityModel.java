package acats.fromanotherworld.entity.model.thing.special;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class AlienThingEntityModel extends GeoModel<AlienThingEntity> {
    @Override
    public ResourceLocation getModelResource(AlienThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/special/alien_thing/" + animatable.currentForm() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlienThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/special/alien_thing/" + animatable.currentForm() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlienThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/special/alien_thing/" + animatable.currentForm() + ".animation.json");
    }
}
