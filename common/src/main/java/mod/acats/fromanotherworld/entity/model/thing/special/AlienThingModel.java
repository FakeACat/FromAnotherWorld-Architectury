package mod.acats.fromanotherworld.entity.model.thing.special;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.special.AlienThing;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class AlienThingModel extends GeoModel<AlienThing> {
    @Override
    public ResourceLocation getModelResource(AlienThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/special/alien_thing/" + animatable.currentForm() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlienThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/special/alien_thing/" + animatable.currentForm() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlienThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/special/alien_thing/" + animatable.currentForm() + ".animation.json");
    }
}
