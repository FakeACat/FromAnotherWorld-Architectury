package acats.fromanotherworld.block;

import acats.fromanotherworld.block.entity.TunnelBlockEntity;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import acats.fromanotherworld.utilities.FAWWaterloggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TunnelBlock extends BaseEntityBlock implements FAWWaterloggable {
    private static final VoxelShape shape = Block.box(0, 0, 0, 16, 4, 16);
    public static final EnumProperty<TentacleState> TENTACLE_STATE;
    public static final BooleanProperty WATERLOGGED;
    public TunnelBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TunnelBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TENTACLE_STATE, WATERLOGGED);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return shape;
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState blockState) {
        return SoundType.GRAVEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), TunnelBlockEntity::tick);
    }

    @Override
    public BooleanProperty getWaterloggedProperty() {
        return WATERLOGGED;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        this.waterloggedNeighbourChanged(blockState, level, blockPos);
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.waterloggedStateForPlacement(this.defaultBlockState(), blockPlaceContext);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return this.waterloggedFluidState(blockState);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        this.waterloggedUpdateShape(blockState, levelAccessor, blockPos);
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public enum TentacleState implements StringRepresentable {
        EMERGING("emerging"),
        ACTIVE("active"),
        RETREATING("retreating");

        private final String name;
        TentacleState(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    static {
        TENTACLE_STATE = EnumProperty.create("tentacles", TentacleState.class);
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
