package mod.acats.fromanotherworld.block.entity;

import mod.acats.fromanotherworld.block.CorpseBlock;
import mod.acats.fromanotherworld.block.interfaces.Gore;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
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

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CorpseBlockEntity blockEntity){
        if (!level.isClientSide() && level.getGameTime() % 100 == 0){
            int size = CorpseBlock.getCorpseType(blockState).getSize();
            BlockUtilities.forEachBlockInCubeCentredAt(blockPos, size, blockPos1 -> {
                if (level.random.nextInt(4) == 0)
                    Gore.attemptPlaceUndergroundGrowth(level, blockPos1, Direction.getRandom(level.getRandom()));
            });
        }
    }
}
