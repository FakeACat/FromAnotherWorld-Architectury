package acats.fromanotherworld.entity.model.thing.revealed;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ChestSpitterEntityModel extends GeoModel<ChestSpitterEntity> {
    @Override
    public Identifier getModelResource(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/revealed/chest_spitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/revealed/chest_spitter/chest_spitter.png");
    }

    @Override
    public Identifier getAnimationResource(ChestSpitterEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/revealed/chest_spitter.animation.json");
    }

    @Override
    public RenderLayer getRenderType(ChestSpitterEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
