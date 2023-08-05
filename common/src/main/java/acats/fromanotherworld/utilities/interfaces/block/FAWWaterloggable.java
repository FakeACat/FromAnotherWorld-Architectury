package acats.fromanotherworld.utilities.interfaces.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface FAWWaterloggable extends SimpleWaterloggedBlock {

    BooleanProperty getWaterloggedProperty();

    default boolean isWaterlogged(BlockState state) {
        return state.getValue(this.getWaterloggedProperty());
    }
    default BlockState setWaterlogged(BlockState state, boolean bl) {
        return state.setValue(this.getWaterloggedProperty(), bl);
    }

    // Call in neighborChanged
    default void waterloggedNeighbourChanged(BlockState blockState, Level level, BlockPos blockPos) {
        if (!level.isClientSide() && this.isWaterlogged(blockState)) {
            level.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
    }

    // Return this in getStateForPlacement
    default BlockState waterloggedStateForPlacement(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.setWaterlogged(blockState, fluidState.getType() == Fluids.WATER);
    }

    // Return this in getFluidState
    default FluidState waterloggedFluidState(BlockState blockState) {
        return this.isWaterlogged(blockState) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    // Call this in updateShape
    default void waterloggedUpdateShape(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        if (this.isWaterlogged(blockState)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
    }
}
