package mod.acats.fromanotherworld.block.entity.model;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkOvergrowthBlockEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class AssimilatedSculkOvergrowthBlockEntityModel extends GeoModel<AssimilatedSculkOvergrowthBlockEntity> {
    @Override
    public ResourceLocation getModelResource(AssimilatedSculkOvergrowthBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/block/sculk/assimilated_sculk_overgrowth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AssimilatedSculkOvergrowthBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/sculk/assimilated_sculk_overgrowth.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AssimilatedSculkOvergrowthBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/block/sculk/assimilated_sculk_overgrowth.animation.json");
    }
}
