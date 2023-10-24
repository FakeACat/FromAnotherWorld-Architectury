package mod.acats.fromanotherworld.block.entity.model;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkTentaclesBlockEntity;
import net.minecraft.resources.ResourceLocation;

public class AssimilatedSculkTentacleHeadModel extends AssimilatedSculkTentaclesBlockEntityModel {
    @Override
    public ResourceLocation getModelResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/block/sculk/assimilated_sculk_tentacle_head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/sculk/assimilated_sculk_tentacle_head.png");
    }

    @Override
    public ResourceLocation getGlowTextureResource() {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/sculk/assimilated_sculk_tentacle_head_glow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AssimilatedSculkTentaclesBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/block/sculk/assimilated_sculk_tentacle_head.animation.json");
    }
}
