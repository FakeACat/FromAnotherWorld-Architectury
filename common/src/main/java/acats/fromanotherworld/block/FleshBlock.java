package acats.fromanotherworld.block;

import acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public abstract class FleshBlock extends FlammableBlock {
    public FleshBlock(Properties settings) {
        super(settings, 15, 50);
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState state) {
        return SoundType.WET_GRASS;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return BlockUtilities.isOnAcceptableSurface(world, pos, Direction.DOWN);
    }
}
