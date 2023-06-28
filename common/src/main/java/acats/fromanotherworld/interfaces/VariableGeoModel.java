package acats.fromanotherworld.interfaces;

import acats.fromanotherworld.FromAnotherWorld;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import net.minecraft.resources.ResourceLocation;

public interface VariableGeoModel<T extends GeoAnimatable> {
    String getVariant(T animatable);
    String getPath();

    default ResourceLocation getVariantModelResource(T animatable){
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/" + this.getPath() + getVariant(animatable) + ".geo.json");
    }
    default ResourceLocation getVariantTextureResource(T animatable){
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/" + this.getPath() + getVariant(animatable) + ".png");
    }
    default ResourceLocation getVariantAnimationResource(T animatable){
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/" + this.getPath() + getVariant(animatable) + ".animation.json");
    }
}
