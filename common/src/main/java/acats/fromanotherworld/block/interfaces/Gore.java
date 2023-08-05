package acats.fromanotherworld.block.interfaces;

import acats.fromanotherworld.block.DisguisedTendrilsBlock;
import acats.fromanotherworld.block.TentacleBlock;
import acats.fromanotherworld.block.WallPalmerBlock;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public interface Gore {
    default boolean connectsHorizontally(BlockState blockState, Direction surface){
        return true;
    }
    default Direction surface(Level level, BlockState blockState){
        return Direction.DOWN;
    }
    default boolean isUnderground(Level level, BlockPos pos) {
        return level.getBrightness(LightLayer.SKY, pos) == 0;
    }
    default void spread(Level level, BlockPos pos, BlockState blockState){
        if (!level.isClientSide()){
            if (this.isUnderground(level, pos)) {
                this.spreadUnderground(level, pos, blockState);
            }
            else {
                this.spreadSurface(level, pos, blockState);
            }
        }
    }

    default void spreadSurface(Level level, BlockPos pos, BlockState blockState) {
        BlockUtilities.forEachBlockInCubeCentredAt(pos, 1, blockPos -> {
            if (level.getRandom().nextInt(3) == 0) {
                this.attemptPlaceSurfaceGrowth(level, blockPos);
            }
        });
    }

    default void attemptPlaceSurfaceGrowth(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).canBeReplaced() || !level.getBlockState(pos).getFluidState().isEmpty()) {
            return;
        }

        BlockState state = DisguisedTendrilsBlock.correctStateAt(BlockRegistry.DISGUISED_TENDRILS.get().defaultBlockState(), level, pos);
        if (state.canSurvive(level, pos)) {
            level.setBlockAndUpdate(pos, state);
        }
    }

    default void spreadUnderground(Level level, BlockPos pos, BlockState blockState) {
        Direction surface = surface(level, blockState);

        this.forEachPossibleTentacleLocation(pos, surface, pos2 -> {
            if (level.getRandom().nextInt(3) == 0) {
                this.attemptPlaceUndergroundGrowth(level, pos2, level.getRandom().nextInt(10) == 0 ? Direction.getRandom(level.getRandom()) : surface);
            }
        });
    }

    default void attemptPlaceUndergroundGrowth(Level level, BlockPos pos, Direction surface){
        BlockState state = TentacleBlock.correctConnectionStates(level, pos, BlockRegistry.TENTACLE.get().defaultBlockState().setValue(TentacleBlock.SURFACE, surface));

        var ref = new Object() {
            int connectedTentacles = 0;
        };
        this.forEachPossibleTentacleLocation(pos, surface, pos2 -> {
            BlockState state2 = level.getBlockState(pos2);
            if (level.getBlockState(pos2).getBlock() instanceof Gore gore && gore.connectsHorizontally(state2, surface)) {
                ref.connectedTentacles++;
            }
        });

        if (ref.connectedTentacles < 2 && level.getBlockState(pos).canBeReplaced() && level.getBlockState(pos).getFluidState().isEmpty() && state.canSurvive(level, pos)){
            if (level.getRandom().nextInt(Config.GORE_CONFIG.wallPalmerChance.get()) == 0 &&
                    surface.getAxis() != Direction.Axis.Y &&
                    level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty() &&
                    level.getBlockState(pos.below()).getCollisionShape(level, pos.below()).isEmpty() &&
                    level.getBlockState(pos.above().relative(surface)).isFaceSturdy(level, pos.above().relative(surface), surface.getOpposite()) &&
                    level.getBlockState(pos.below().relative(surface)).isFaceSturdy(level, pos.below().relative(surface), surface.getOpposite())){

                level.setBlockAndUpdate(pos, WallPalmerBlock.facing(BlockRegistry.WALL_PALMER.get().defaultBlockState(), surface.getOpposite()));
                return;
            }
            level.setBlockAndUpdate(pos, state);
        }
    }
    default void forEachPossibleTentacleLocation(BlockPos pos, Direction surface, Consumer<BlockPos> function){
        for (Direction direction:
                Direction.values()) {
            if (!direction.equals(surface) && !direction.equals(surface.getOpposite())) {
                BlockPos newPos = pos.relative(direction);
                function.accept(newPos.relative(surface.getOpposite()));
                function.accept(newPos);
                function.accept(newPos.relative(surface));
            }
        }
    }
}
