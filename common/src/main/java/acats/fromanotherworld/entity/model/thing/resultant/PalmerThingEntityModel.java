package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class PalmerThingEntityModel extends GeoModel<PalmerThingEntity> {
    @Override
    public Identifier getModelResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/palmer_thing.geo.json");
    }

    @Override
    public Identifier getTextureResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing.png");
    }

    @Override
    public Identifier getAnimationResource(PalmerThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/palmer_thing.animation.json");
    }
}
