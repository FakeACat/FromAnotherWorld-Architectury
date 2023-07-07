package acats.fromanotherworld.block;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import acats.fromanotherworld.block.interfaces.Gore;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CorpseBlock extends BaseEntityBlock implements Gore {
    public static final EnumProperty<CorpseType> TYPE = EnumProperty.create("type", CorpseType.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CorpseBlock(Properties properties) {
        super(properties);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
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
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), CorpseBlockEntity::tick);
    }

    public static CorpseType getCorpseType(BlockState state){
        return state.getValue(TYPE);
    }
    public static void setCorpseType(BlockState state, CorpseType corpseType){
        state.setValue(TYPE, corpseType);
    }

    public enum CorpseType implements StringRepresentable {
        HUMAN_1("human_1", 5),
        SMALL_1("small_1", 3);

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
}
