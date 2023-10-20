package mod.acats.fromanotherworld.block.entity.model;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkTentaclesBlockEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class AssimilatedSculkTentacleSpiderModel extends GeoModel<AssimilatedSculkTentaclesBlockEntity> {
    @Override
    public ResourceLocation getModelResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/block/sculk/assimilated_sculk_tentacle_spider.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/sculk/assimilated_sculk_tentacle_spider.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/block/sculk/assimilated_sculk_tentacle_spider.animation.json");
    }
}
