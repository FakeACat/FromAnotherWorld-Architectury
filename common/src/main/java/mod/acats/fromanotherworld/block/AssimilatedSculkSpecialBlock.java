package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.entity.AssimilatedSculkBlockEntity;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AssimilatedSculkSpecialBlock extends BaseEntityBlock implements AssimilatedSculk {
    private static final BooleanProperty REVEALED;
    protected AssimilatedSculkSpecialBlock(Properties properties) {
        super(properties);
    }

    protected abstract BlockEntityType<?> getBlockEntityType();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVEALED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.disguisedBlockState();
    }

    public BlockState disguisedBlockState() {
        return setRevealed(this.defaultBlockState(), false);
    }

    public static boolean getRevealed(BlockState blockState) {
        return blockState.getValue(REVEALED);
    }
    public static BlockState setRevealed(BlockState blockState, boolean bl) {
        return blockState.setValue(REVEALED, bl);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.getBlockEntityType().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return createTickerHelper(blockEntityType, this.getBlockEntityType(), ((level1, blockPos, blockState1, blockEntity) ->
                    ((AssimilatedSculkBlockEntity) blockEntity).clientTick(level1, blockPos, blockState1)));
        }
        else {
            return createTickerHelper(blockEntityType, this.getBlockEntityType(), ((level1, blockPos, blockState1, blockEntity) ->
                    ((AssimilatedSculkBlockEntity) blockEntity).serverTick(level1, blockPos, blockState1)));
        }
    }


    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return getRevealed(blockState) ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
    }

    @Override
    public void reveal(Level level, BlockPos pos, BlockState blockState) {
        if (!getRevealed(blockState)) {
            level.setBlockAndUpdate(pos, setRevealed(blockState, true));
            AssimilatedSculk.revealNear(level, pos);
        }
    }

    static {
        REVEALED = BooleanProperty.create("revealed");
    }
}
