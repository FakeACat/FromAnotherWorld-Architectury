package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculkBehaviour;
import mod.acats.fromanotherworld.block.spreading.AssimilatedSculkSpreader;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@SuppressWarnings("deprecation")
public class AssimilatedSculkBlock extends SculkBlock implements AssimilatedSculkBehaviour, AssimilatedSculk {
    public AssimilatedSculkBlock(Properties properties) {
        super(properties);
    }

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
        return this.defaultBlockState().setValue(REVEALED, false);
    }

    @Override
    public int attemptAssimilatedUseCharge(AssimilatedSculkSpreader.AssimilatedChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, AssimilatedSculkSpreader sculkSpreader, boolean bl) {
        int i = chargeCursor.getCharge();
        if (i != 0 && randomSource.nextInt(sculkSpreader.chargeDecayRate()) == 0) {
            BlockPos blockPos2 = chargeCursor.getPos();
            boolean bl2 = blockPos2.closerThan(blockPos, sculkSpreader.noGrowthRadius());
            if (!bl2 && canPlaceGrowth(levelAccessor, blockPos2)) {
                int j = sculkSpreader.growthSpawnCost();
                if (randomSource.nextInt(j) < i) {
                    BlockPos blockPos3 = blockPos2.above();
                    BlockState blockState = this.getRandomGrowthState(levelAccessor, blockPos3, randomSource, sculkSpreader.isWorldGeneration());
                    levelAccessor.setBlock(blockPos3, blockState, 3);
                    levelAccessor.playSound(null, blockPos2, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return randomSource.nextInt(sculkSpreader.additionalDecayRate()) != 0 ? i : i - (bl2 ? 1 : getDecayPenalty(sculkSpreader, blockPos2, blockPos, i));
            }
        } else {
            return i;
        }
    }

    private static int getDecayPenalty(AssimilatedSculkSpreader sculkSpreader, BlockPos blockPos, BlockPos blockPos2, int i) {
        int j = sculkSpreader.noGrowthRadius();
        float f = Mth.square((float)Math.sqrt(blockPos.distSqr(blockPos2)) - (float)j);
        int k = Mth.square(AssimilatedSculkSpreader.MAX_GROWTH_RATE_RADIUS - j);
        float g = Math.min(1.0F, f / (float)k);
        return Math.max(1, (int)((float)i * g * AssimilatedSculkSpreader.MAX_DECAY_FACTOR));
    }

    private BlockState getRandomGrowthState(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, boolean bl) {
        BlockState blockState;
        if (randomSource.nextInt(40) == 0) {
            blockState = BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().disguisedBlockState();
        } else if (randomSource.nextInt(11) == 0) {
            blockState = BlockRegistry.ASSIMILATED_SCULK_OVERGROWTH.get().disguisedBlockState();
        } else {
            blockState = BlockRegistry.ASSIMILATED_SCULK_ACTIVATOR.get().disguisedBlockState();
        }

        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !levelAccessor.getFluidState(blockPos).isEmpty() ? blockState.setValue(BlockStateProperties.WATERLOGGED, true) : blockState;
    }

    private static boolean canPlaceGrowth(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockState blockState = levelAccessor.getBlockState(blockPos.above());
        if (blockState.isAir() || blockState.is(Blocks.WATER) && blockState.getFluidState().is(Fluids.WATER)) {
            int i = 0;
            Iterator<BlockPos> var4 = BlockPos.betweenClosed(blockPos.offset(-4, 0, -4), blockPos.offset(4, 2, 4)).iterator();

            do {
                if (!var4.hasNext()) {
                    return true;
                }

                BlockPos blockPos2 = var4.next();
                BlockState blockState2 = levelAccessor.getBlockState(blockPos2);
                if (blockState2.getBlock() instanceof AssimilatedSculkSpecialBlock) {
                    ++i;
                }
            } while(i <= 2);

        }
        return false;
    }

    @Override
    public boolean canAssimilatedChangeBlockStateOnSpread() {
        return false;
    }

    @Override
    public void reveal(Level level, BlockPos pos, BlockState blockState) {
        level.setBlock(pos, blockState.setValue(REVEALED, true), 3);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        AssimilatedSculk.assimilateSurroundingSculk(serverLevel, blockPos);
    }

    @Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        AssimilatedSculk.alert(serverLevel, blockPos);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);

        if (level.getRandom().nextInt(60) == 0 && !(entity instanceof Player)) {
            EntityUtilities.assimilate(entity);
        }
    }
}
