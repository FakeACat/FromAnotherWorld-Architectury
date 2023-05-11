package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.DogBeastEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class DogBeastEntityModel extends GeoModel<DogBeastEntity> {
    @Override
    public Identifier getModelResource(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/dogbeast.geo.json");
    }

    @Override
    public Identifier getTextureResource(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/dogbeast.png");
    }

    @Override
    public Identifier getAnimationResource(DogBeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/dogbeast.animation.json");
    }

    @Override
    public RenderLayer getRenderType(DogBeastEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
