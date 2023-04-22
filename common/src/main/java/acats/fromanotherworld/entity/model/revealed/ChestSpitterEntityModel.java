package acats.fromanotherworld.entity.model.revealed;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ChestSpitterEntityModel extends GeoModel<ChestSpitterEntity> {
    @Override
    public Identifier getModelResource(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/revealed/chest_spitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/revealed/chest_spitter/chest_spitter.png");
    }

    @Override
    public Identifier getAnimationResource(ChestSpitterEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/revealed/chest_spitter.animation.json");
    }

    @Override
    public RenderLayer getRenderType(ChestSpitterEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
