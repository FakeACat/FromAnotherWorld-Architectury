package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.BeastEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BeastEntityModel extends GeoModel<BeastEntity> {
    @Override
    public ResourceLocation getModelResource(BeastEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/beast.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeastEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/beast.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeastEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/beast.animation.json");
    }

    @Override
    public RenderType getRenderType(BeastEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
