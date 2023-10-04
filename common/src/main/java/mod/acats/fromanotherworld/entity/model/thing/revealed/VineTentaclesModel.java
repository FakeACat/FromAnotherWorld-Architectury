package mod.acats.fromanotherworld.entity.model.thing.revealed;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.revealed.VineTentacles;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class VineTentaclesModel extends GeoModel<VineTentacles> {
    @Override
    public ResourceLocation getModelResource(VineTentacles animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/revealed/vine_tentacles.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VineTentacles animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/revealed/vine_tentacles.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VineTentacles animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/revealed/vine_tentacles.animation.json");
    }
}
