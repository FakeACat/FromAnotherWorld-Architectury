package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.BeastEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class BeastEntityModel extends GeoModel<BeastEntity> {
    @Override
    public Identifier getModelResource(BeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/beast.geo.json");
    }

    @Override
    public Identifier getTextureResource(BeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/beast.png");
    }

    @Override
    public Identifier getAnimationResource(BeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/beast.animation.json");
    }

    @Override
    public RenderLayer getRenderType(BeastEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
