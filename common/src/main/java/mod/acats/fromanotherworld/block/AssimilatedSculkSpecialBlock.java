package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.entity.AssimilatedSculkBlockEntity;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.entity.interfaces.SimSculkObservable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class AssimilatedSculkSpecialBlock extends BaseEntityBlock implements AssimilatedSculk {
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
        return revealed(blockState) ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
    }

    @Override
    public void reveal(Level level, BlockPos pos, BlockState blockState, float strength) {
        level.setBlock(pos, setRevealed(blockState, true), 3);
        AssimilatedSculk.alert(level, pos, strength * 0.75F);
    }

    protected boolean canRedisguiseRandomly() {
        return true;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        AssimilatedSculk.assimilateSurroundingSculk(serverLevel, blockPos);

        if (this.revealed(blockState) && this.canRedisguiseRandomly() && serverLevel.getRandom().nextInt(REDISGUISE_CHANCE) == 0) {
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(REVEALED, false));
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (entity instanceof Player) {
            if (!this.revealed(blockState)) {
                AssimilatedSculk.alert(level, blockPos, 1.0F);
            }
            ((SimSculkObservable) entity).faw$setObservationTime(60);
        }
    }

    @Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        AssimilatedSculk.alert(serverLevel, blockPos, 1.0F);
    }
}
