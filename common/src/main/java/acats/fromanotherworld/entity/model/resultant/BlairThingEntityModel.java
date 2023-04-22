package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.BlairThingEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BlairThingEntityModel extends GeoModel<BlairThingEntity> {
    @Override
    public Identifier getModelResource(BlairThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/blair_thing.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlairThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/blair_thing.png");
    }

    @Override
    public Identifier getAnimationResource(BlairThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/blair_thing.animation.json");
    }

    @Override
    public RenderLayer getRenderType(BlairThingEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
