package mod.acats.fromanotherworld.block.entity.model;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.TunnelBlockEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TunnelBlockEntityModel extends GeoModel<TunnelBlockEntity> {
    @Override
    public ResourceLocation getModelResource(TunnelBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/block/tunnel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TunnelBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/tunnel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TunnelBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/block/tunnel.animation.json");
    }
}
