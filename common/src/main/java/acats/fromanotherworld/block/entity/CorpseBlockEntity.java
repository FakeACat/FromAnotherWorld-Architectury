package acats.fromanotherworld.block.entity;

import acats.fromanotherworld.block.CorpseBlock;
import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CorpseBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);
    public CorpseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public CorpseBlock.CorpseType getCorpseType(){
        return CorpseBlock.getCorpseType(this.getBlockState());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FAWAnimations.blockAlwaysPlaying(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
}
