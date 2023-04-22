package acats.fromanotherworld.entity.model.special;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.special.AlienThingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AlienThingEntityModel extends GeoModel<AlienThingEntity> {
    @Override
    public Identifier getModelResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/special/alien_thing/" + animatable.currentForm() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/special/alien_thing/" + animatable.currentForm() + ".png");
    }

    @Override
    public Identifier getAnimationResource(AlienThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/special/alien_thing/" + animatable.currentForm() + ".animation.json");
    }
}
