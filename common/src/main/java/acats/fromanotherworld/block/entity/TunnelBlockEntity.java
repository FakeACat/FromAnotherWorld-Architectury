package acats.fromanotherworld.block.entity;

import acats.fromanotherworld.block.TunnelBlock;
import acats.fromanotherworld.block.interfaces.Gore;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.constants.TimeInTicks;
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
import net.minecraft.nbt.CompoundTag;
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

    private long age;

    public void deactivate() {
        if (this.level != null && this.getBlockState().getValue(TunnelBlock.TENTACLE_STATE) == TunnelBlock.TentacleState.ACTIVE) {
            this.age = -60;
            this.level.setBlockAndUpdate(
                    this.getBlockPos(),
                    BlockRegistry.TUNNEL_BLOCK.get()
                            .defaultBlockState()
                            .setValue(TunnelBlock.TENTACLE_STATE, TunnelBlock.TentacleState.RETREATING)
                            .setValue(TunnelBlock.WATERLOGGED, this.getBlockState().getValue(TunnelBlock.WATERLOGGED)));
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, TunnelBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        blockEntity.age++;
        if (blockEntity.age % TimeInTicks.SECOND == 0) {
            if (blockEntity.age == 0 && blockState.getValue(TunnelBlock.TENTACLE_STATE) == TunnelBlock.TentacleState.RETREATING) {
                level.destroyBlock(blockPos, false);
                return;
            }

            BlockUtilities.grief(level, 1, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, 1);
            if (blockState.getValue(TunnelBlock.TENTACLE_STATE) == TunnelBlock.TentacleState.EMERGING) {
                level.setBlockAndUpdate(blockPos, BlockRegistry.TUNNEL_BLOCK.get().defaultBlockState().setValue(TunnelBlock.TENTACLE_STATE, TunnelBlock.TentacleState.ACTIVE).setValue(TunnelBlock.WATERLOGGED, blockState.getValue(TunnelBlock.WATERLOGGED)));
            }

            int minGoreAge = Config.GORE_CONFIG.tunnelGoreTime.get();
            if (minGoreAge > -1 && blockEntity.age > minGoreAge && level.getRandom().nextInt(300) == 0) {
                Gore gore = (Gore) blockState.getBlock();
                gore.spread(level, blockPos, blockState);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putLong("Age", this.age);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.age = compoundTag.getLong("Age");
    }
}
