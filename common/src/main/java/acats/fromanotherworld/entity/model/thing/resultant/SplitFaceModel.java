package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.SplitFace;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class SplitFaceModel extends GeoModel<SplitFace> {
    @Override
    public ResourceLocation getModelResource(SplitFace animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/split_face.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SplitFace animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/split_face.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SplitFace animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/split_face.animation.json");
    }
}
