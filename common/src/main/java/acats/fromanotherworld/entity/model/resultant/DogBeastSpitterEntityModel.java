package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.DogBeastSpitterEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class DogBeastSpitterEntityModel extends GeoModel<DogBeastSpitterEntity> {
    @Override
    public Identifier getModelResource(DogBeastSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/dogbeast_spitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(DogBeastSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/dogbeast_spitter.png");
    }

    @Override
    public Identifier getAnimationResource(DogBeastSpitterEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/dogbeast_spitter.animation.json");
    }

    @Override
    public RenderLayer getRenderType(DogBeastSpitterEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
