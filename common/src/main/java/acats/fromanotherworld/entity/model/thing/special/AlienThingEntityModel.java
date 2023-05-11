package acats.fromanotherworld.entity.model.thing.special;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class AlienThingEntityModel extends GeoModel<AlienThingEntity> {
    @Override
    public Identifier getModelResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/special/alien_thing/" + animatable.currentForm() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/special/alien_thing/" + animatable.currentForm() + ".png");
    }

    @Override
    public Identifier getAnimationResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/special/alien_thing/" + animatable.currentForm() + ".animation.json");
    }
}
