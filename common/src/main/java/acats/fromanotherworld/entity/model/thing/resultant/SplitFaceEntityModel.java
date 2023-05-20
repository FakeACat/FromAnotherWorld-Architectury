package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.SplitFaceEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class SplitFaceEntityModel extends GeoModel<SplitFaceEntity> {
    @Override
    public Identifier getModelResource(SplitFaceEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/split_face.geo.json");
    }

    @Override
    public Identifier getTextureResource(SplitFaceEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/split_face.png");
    }

    @Override
    public Identifier getAnimationResource(SplitFaceEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/split_face.animation.json");
    }
}
