package mod.acats.fromanotherworld.block.entity.model;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkActivatorBlockEntity;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AssimilatedSculkActivatorBlockEntityModel extends GeoModel<AssimilatedSculkActivatorBlockEntity> {
    @Override
    public ResourceLocation getModelResource(AssimilatedSculkActivatorBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/block/sculk/assimilated_sculk_activator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AssimilatedSculkActivatorBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/sculk/assimilated_sculk_activator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AssimilatedSculkActivatorBlockEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/block/sculk/assimilated_sculk_activator.animation.json");
    }

    @Override
    public void setCustomAnimations(AssimilatedSculkActivatorBlockEntity animatable, long instanceId, AnimationState<AssimilatedSculkActivatorBlockEntity> animationState) {

        CoreGeoBone eye = this.getAnimationProcessor().getBone("eye");
        eye.setRotY(Mth.rotLerp(animationState.getPartialTick(), animatable.clientPrevYaw, animatable.clientYaw) * Mth.DEG_TO_RAD);

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
