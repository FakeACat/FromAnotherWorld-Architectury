package mod.acats.fromanotherworld.block.entity;

import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class AssimilatedSculkOvergrowthBlockEntity extends AssimilatedSculkBlockEntity {
    public static final int RANGE = 16;
    public AssimilatedSculkOvergrowthBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ASSIMILATED_SCULK_OVERGROWTH_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState) {
        if (!blockState.getValue(AssimilatedSculk.REVEALED)) {
            return;
        }

        if (level.getRandom().nextInt(20) == 0 && this.getClosestObservedEntity(RANGE) != null) {
            final List<BlockPos> currentBrambles = NonNullList.create();

            BlockUtilities.forEachBlockInCubeCentredAt(blockPos, RANGE, blockPos1 -> {
                if (blockPos1.distSqr(blockPos) < RANGE * RANGE && brambleSource(level.getBlockState(blockPos1))) {
                    currentBrambles.add(new BlockPos(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ()));
                }
            });

            currentBrambles.forEach(blockPos1 -> {
                for (Direction direction:
                        Direction.values()) {
                    attemptPlaceBrambles(level, blockPos1.relative(direction));
                }
            });
        }
    }

    private static boolean brambleSource(BlockState blockState) {
        return blockState.is(BlockRegistry.ASSIMILATED_SCULK_OVERGROWTH.get()) ||
                blockState.is(BlockRegistry.ASSIMILATED_SCULK_BRAMBLES.get());
    }

    private static void attemptPlaceBrambles(Level level, BlockPos blockPos) {
        if (level.getRandom().nextInt(20) != 0) {
            return;
        }
        BlockState brambles = BlockRegistry.ASSIMILATED_SCULK_BRAMBLES.get().defaultBlockState();
        BlockState currentState = level.getBlockState(blockPos);
        if (currentState.is(Blocks.AIR) || currentState.canBeReplaced()) {
            level.setBlockAndUpdate(blockPos, brambles);
            level.playSound(null, blockPos, brambles.getSoundType().getPlaceSound(), SoundSource.BLOCKS, brambles.getSoundType().getVolume(), brambles.getSoundType().getPitch());
        }
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}
