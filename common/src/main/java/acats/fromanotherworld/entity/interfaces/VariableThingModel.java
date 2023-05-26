package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.FromAnotherWorld;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import net.minecraft.util.Identifier;

public interface VariableThingModel<T extends GeoAnimatable> {
    String getVariant(T animatable);
    String getPath();

    default Identifier getVariantModelResource(T animatable){
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/" + this.getPath() + getVariant(animatable) + ".geo.json");
    }
    default Identifier getVariantTextureResource(T animatable){
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/" + this.getPath() + getVariant(animatable) + ".png");
    }
    default Identifier getVariantAnimationResource(T animatable){
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/" + this.getPath() + getVariant(animatable) + ".animation.json");
    }
}
