package acats.fromanotherworld.block;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import acats.fromanotherworld.block.interfaces.Gore;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import acats.fromanotherworld.utilities.interfaces.block.FAWWaterloggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CorpseBlock extends BaseEntityBlock implements Gore, FAWWaterloggable {
    public static final EnumProperty<CorpseType> TYPE = EnumProperty.create("type", CorpseType.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED;

    public CorpseBlock(Properties properties) {
        super(properties);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING, WATERLOGGED);
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState state) {
        return SoundType.WET_GRASS;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CorpseBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getHorizontalDirection().getOpposite();
        return this.waterloggedStateForPlacement(this.defaultBlockState().setValue(FACING, direction), blockPlaceContext);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), CorpseBlockEntity::tick);
    }

    public static CorpseType getCorpseType(BlockState state){
        return state.getValue(TYPE);
    }
    public static BlockState setCorpseType(BlockState state, CorpseType corpseType){
        state = state.setValue(TYPE, corpseType);
        return state;
    }

    @Override
    public boolean connectsHorizontally(BlockState blockState, Direction surface) {
        return surface == Direction.DOWN;
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

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return this.waterloggedFluidState(blockState);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        this.waterloggedUpdateShape(blockState, levelAccessor, blockPos);
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public enum CorpseType implements StringRepresentable {
        HUMAN_1("human_1", 2),
        SMALL_1("small_1", 1),
        MEDIUM_1("medium_1", 2);

        private final int size;
        private final String name;
        CorpseType(String name, int size){
            this.name = name;
            this.size = size;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
        public int getSize(){
            return this.size;
        }
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
