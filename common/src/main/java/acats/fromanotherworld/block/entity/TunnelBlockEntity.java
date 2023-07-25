package acats.fromanotherworld.block.entity;

import acats.fromanotherworld.block.TunnelBlock;
import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.utilities.BlockUtilities;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TunnelBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);
    private static AnimationController<TunnelBlockEntity> controller(TunnelBlockEntity blockEntity){
        return new AnimationController<>(blockEntity, "TunnelBlockEntity", 0, (event) -> {
            switch (blockEntity.getBlockState().getValue(TunnelBlock.TENTACLE_STATE)) {
                case EMERGING -> event.getController().setAnimation(FAWAnimations.EMERGE);
                case RETREATING -> event.getController().setAnimation(FAWAnimations.BURROW);
                default -> event.getController().setAnimation(FAWAnimations.IDLE);
            }
            return PlayState.CONTINUE;
        });
    }
    public TunnelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(controller(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    private int age;

    public void deactivate() {
        if (this.level != null) {
            this.age = 0;
            this.level.setBlockAndUpdate(this.getBlockPos(), BlockRegistry.TUNNEL_BLOCK.get().defaultBlockState().setValue(TunnelBlock.TENTACLE_STATE, TunnelBlock.TentacleState.RETREATING));
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, TunnelBlockEntity blockEntity) {
        blockEntity.age++;
        if (blockEntity.age % 20 == 0) {
            if (blockState.getValue(TunnelBlock.TENTACLE_STATE) == TunnelBlock.TentacleState.RETREATING) {
                level.destroyBlock(blockPos, false);
                return;
            }

            BlockUtilities.grief(level, 1, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, 1);
            if (blockState.getValue(TunnelBlock.TENTACLE_STATE) == TunnelBlock.TentacleState.EMERGING) {
                level.setBlockAndUpdate(blockPos, BlockRegistry.TUNNEL_BLOCK.get().defaultBlockState().setValue(TunnelBlock.TENTACLE_STATE, TunnelBlock.TentacleState.ACTIVE).setValue(TunnelBlock.WATERLOGGED, blockState.getValue(TunnelBlock.WATERLOGGED)));
            }
        }
    }
}
