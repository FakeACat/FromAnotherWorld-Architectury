package mod.acats.fromanotherworld.entity.model.misc;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.misc.Starship;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class StarshipModel extends GeoModel<Starship> {
    @Override
    public ResourceLocation getModelResource(Starship animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/misc/starship.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Starship animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/misc/starship.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Starship animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/misc/starship.animation.json");
    }
}
