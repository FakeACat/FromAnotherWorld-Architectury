package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class DogBeastEntityModel extends GeoModel<DogBeastEntity> {
    @Override
    public Identifier getModelResource(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/dogbeast.geo.json");
    }

    @Override
    public Identifier getTextureResource(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/dogbeast.png");
    }

    @Override
    public Identifier getAnimationResource(DogBeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/dogbeast.animation.json");
    }

    @Override
    public RenderLayer getRenderType(DogBeastEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
