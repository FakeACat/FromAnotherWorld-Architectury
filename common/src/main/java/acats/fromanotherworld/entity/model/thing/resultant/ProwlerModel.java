package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.Prowler;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class ProwlerModel extends GeoModel<Prowler> {
    @Override
    public ResourceLocation getModelResource(Prowler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/prowler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Prowler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/prowler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Prowler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/prowler.animation.json");
    }
}
