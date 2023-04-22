package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.PalmerThingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class PalmerThingEntityModel extends GeoModel<PalmerThingEntity> {
    @Override
    public Identifier getModelResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/palmer_thing.geo.json");
    }

    @Override
    public Identifier getTextureResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/palmer_thing/palmer_thing.png");
    }

    @Override
    public Identifier getAnimationResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/palmer_thing.animation.json");
    }
}
