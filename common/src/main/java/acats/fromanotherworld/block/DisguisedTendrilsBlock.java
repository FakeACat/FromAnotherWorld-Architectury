package acats.fromanotherworld.block;

import acats.fromanotherworld.block.interfaces.Gore;
import acats.fromanotherworld.utilities.BlockUtilities;
import acats.fromanotherworld.utilities.interfaces.block.CustomColourProviderBlock;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class DisguisedTendrilsBlock extends FleshBlock implements Gore, CustomColourProviderBlock {
    private static final BooleanProperty NORTH;
    private static final BooleanProperty EAST;
    private static final BooleanProperty SOUTH;
    private static final BooleanProperty WEST;
    private static final BooleanProperty UP;
    private static final BooleanProperty DOWN;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape DOWN_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;
    public DisguisedTendrilsBlock(Properties settings) {
        super(settings);
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), DisguisedTendrilsBlock::calculateShape)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    private static VoxelShape calculateShape(BlockState blockState) {
        VoxelShape voxelShape = Shapes.empty();
        if (blockState.getValue(UP)) {
            voxelShape = UP_AABB;
        }

        if (blockState.getValue(DOWN)) {
            voxelShape = DOWN_AABB;
        }

        if (blockState.getValue(NORTH)) {
            voxelShape = Shapes.or(voxelShape, NORTH_AABB);
        }

        if (blockState.getValue(SOUTH)) {
            voxelShape = Shapes.or(voxelShape, SOUTH_AABB);
        }

        if (blockState.getValue(EAST)) {
            voxelShape = Shapes.or(voxelShape, EAST_AABB);
        }

        if (blockState.getValue(WEST)) {
            voxelShape = Shapes.or(voxelShape, WEST_AABB);
        }

        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.shapesCache.get(blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return correctStateAt(this.defaultBlockState(), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    }

    public static BlockState correctStateAt(BlockState initialState, Level level, BlockPos blockPos) {
        return initialState
                .setValue(NORTH, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.NORTH))
                .setValue(EAST, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.EAST))
                .setValue(SOUTH, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.SOUTH))
                .setValue(WEST, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.WEST))
                .setValue(UP, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.UP))
                .setValue(DOWN, BlockUtilities.isOnAcceptableSurface(level, blockPos, Direction.DOWN));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        level.setBlockAndUpdate(blockPos, correctStateAt(blockState, level, blockPos));
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90 ->
                    blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default -> blockState;
        };
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT ->
                    blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK ->
                    blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default -> super.mirror(blockState, mirror);
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        for (Direction dir:
             Direction.values()) {
            if (BlockUtilities.isOnAcceptableSurface(world, pos, dir)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (serverLevel.getRandom().nextInt(4) == 0) {
            this.spread(serverLevel, blockPos, blockState);
        }
        super.tick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public boolean connectsHorizontally(BlockState blockState, Direction surface) {
        return false;
    }

    @Override
    public Direction surface(Level level, BlockState blockState) {
        return Direction.getRandom(level.getRandom());
    }

    @Override
    public int getDefaultColour() {
        return GrassColor.getDefaultColor();
    }

    @Override
    public int getColour(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, int i) {
        return BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos);
    }

    static {
        NORTH = BooleanProperty.create("north");
        EAST = BooleanProperty.create("east");
        SOUTH = BooleanProperty.create("south");
        WEST = BooleanProperty.create("west");
        UP = BooleanProperty.create("up");
        DOWN = BooleanProperty.create("down");

        UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
