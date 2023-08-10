package mod.acats.fromanotherworld.entity.model.misc;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.misc.StarshipEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class StarshipModel extends GeoModel<StarshipEntity> {
    @Override
    public ResourceLocation getModelResource(StarshipEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/misc/starship.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StarshipEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/misc/starship.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StarshipEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/misc/starship.animation.json");
    }
}
