package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculkBehaviour;
import mod.acats.fromanotherworld.block.spreading.AssimilatedSculkSpreader;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings("deprecation")
public class AssimilatedSculkVeinBlock extends SculkVeinBlock implements AssimilatedSculkBehaviour, AssimilatedSculk {
    private final MultifaceSpreader assimilatedVeinSpreader;
    private final MultifaceSpreader sameSpaceAssimilatedVeinSpreader;
    public AssimilatedSculkVeinBlock(Properties properties) {
        super(properties);
        this.assimilatedVeinSpreader = new MultifaceSpreader(new AssimilatedSculkVeinSpreaderConfig(MultifaceSpreader.DEFAULT_SPREAD_ORDER));
        this.sameSpaceAssimilatedVeinSpreader = new MultifaceSpreader(new AssimilatedSculkVeinSpreaderConfig(MultifaceSpreader.SpreadType.SAME_POSITION));
        this.registerDefaultState(this.defaultBlockState().setValue(REVEALED, false));
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return this.assimilatedVeinSpreader;
    }

    @Override
    public @NotNull MultifaceSpreader getSameSpaceSpreader() {
        return this.sameSpaceAssimilatedVeinSpreader;
    }

    public static boolean assimilatedRegrow(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Collection<Direction> collection) {
        boolean bl = false;
        BlockState blockState2 = BlockRegistry.ASSIMILATED_SCULK_VEIN.get().defaultBlockState();

        for (Direction direction : collection) {
            BlockPos blockPos2 = blockPos.relative(direction);
            if (canAttachTo(levelAccessor, direction, blockPos2, levelAccessor.getBlockState(blockPos2))) {
                blockState2 = blockState2.setValue(getFaceProperty(direction), true);
                bl = true;
            }
        }

        if (!bl) {
            return false;
        } else {
            if (!blockState.getFluidState().isEmpty()) {
                blockState2 = blockState2.setValue(BlockStateProperties.WATERLOGGED, true);
            }

            levelAccessor.setBlock(blockPos, blockState2, 3);
            return true;
        }
    }

    public static boolean hasAssimilatedSubstrateAccess(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos) {
        if (blockState.is(BlockRegistry.ASSIMILATED_SCULK_VEIN.get())) {
            for (Direction direction : DIRECTIONS) {
                if (hasFace(blockState, direction) && levelAccessor.getBlockState(blockPos.relative(direction)).is(mod.acats.fromanotherworld.tags.BlockTags.ASSIMILATED_SCULK_REPLACEABLE)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int attemptAssimilatedUseCharge(AssimilatedSculkSpreader.AssimilatedChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, AssimilatedSculkSpreader sculkSpreader, boolean bl) {
        if (bl && this.attemptPlaceSculk(sculkSpreader, levelAccessor, chargeCursor.getPos(), randomSource)) {
            return chargeCursor.getCharge() - 1;
        } else {
            return randomSource.nextInt(sculkSpreader.chargeDecayRate()) == 0 ? Mth.floor((float)chargeCursor.getCharge() * 0.5F) : chargeCursor.getCharge();
        }
    }

    private boolean attemptPlaceSculk(AssimilatedSculkSpreader sculkSpreader, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource) {
        BlockState blockState = levelAccessor.getBlockState(blockPos);
        TagKey<Block> tagKey = sculkSpreader.replaceableBlocks();

        for (Direction direction : Direction.allShuffled(randomSource)) {
            if (hasFace(blockState, direction)) {
                BlockPos blockPos2 = blockPos.relative(direction);
                BlockState blockState2 = levelAccessor.getBlockState(blockPos2);
                if (blockState2.is(tagKey)) {
                    BlockState blockState3 = BlockRegistry.ASSIMILATED_SCULK.get().disguisedBlockState();
                    levelAccessor.setBlock(blockPos2, blockState3, 3);
                    Block.pushEntitiesUp(blockState2, blockState3, levelAccessor, blockPos2);
                    levelAccessor.playSound(null, blockPos2, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.assimilatedVeinSpreader.spreadAll(blockState3, levelAccessor, blockPos2, sculkSpreader.isWorldGeneration());
                    Direction direction2 = direction.getOpposite();

                    for (Direction direction3 : DIRECTIONS) {
                        if (direction3 != direction2) {
                            BlockPos blockPos3 = blockPos2.relative(direction3);
                            BlockState blockState4 = levelAccessor.getBlockState(blockPos3);
                            if (blockState4.is(this)) {
                                this.onAssimilatedDischarged(levelAccessor, blockState4, blockPos3, randomSource);
                            }
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onAssimilatedDischarged(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.is(this)) {

            for (Direction direction : DIRECTIONS) {
                BooleanProperty booleanProperty = getFaceProperty(direction);
                if (blockState.getValue(booleanProperty) && levelAccessor.getBlockState(blockPos.relative(direction)).is(BlockRegistry.ASSIMILATED_SCULK.get())) {
                    blockState = blockState.setValue(booleanProperty, false);
                }
            }

            if (!hasAnyFace(blockState)) {
                FluidState fluidState = levelAccessor.getFluidState(blockPos);
                blockState = (fluidState.isEmpty() ? Blocks.AIR : Blocks.WATER).defaultBlockState();
            }

            levelAccessor.setBlock(blockPos, blockState, 3);
            AssimilatedSculkBehaviour.super.onAssimilatedDischarged(levelAccessor, blockState, blockPos, randomSource);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        AssimilatedSculk.assimilateSurroundingSculk(serverLevel, blockPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(REVEALED);
    }

    @Override
    public void reveal(Level level, BlockPos pos, BlockState blockState) {
        level.setBlock(pos, blockState.setValue(REVEALED, true), 3);
    }

    /*@Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        AssimilatedSculk.alert(serverLevel, blockPos);
    }*/

    private class AssimilatedSculkVeinSpreaderConfig extends MultifaceSpreader.DefaultSpreaderConfig {
        private final MultifaceSpreader.SpreadType[] spreadTypes;

        public AssimilatedSculkVeinSpreaderConfig(MultifaceSpreader.SpreadType... spreadTypes) {
            super(AssimilatedSculkVeinBlock.this);
            this.spreadTypes = spreadTypes;
        }

        public boolean stateCanBeReplaced(BlockGetter blockGetter, BlockPos blockPos, BlockPos blockPos2, Direction direction, BlockState blockState) {
            BlockState blockState2 = blockGetter.getBlockState(blockPos2.relative(direction));
            if (!blockState2.is(BlockRegistry.ASSIMILATED_SCULK.get()) && !blockState2.is(Blocks.MOVING_PISTON)) {
                if (blockPos.distManhattan(blockPos2) == 2) {
                    BlockPos blockPos3 = blockPos.relative(direction.getOpposite());
                    if (blockGetter.getBlockState(blockPos3).isFaceSturdy(blockGetter, blockPos3, direction)) {
                        return false;
                    }
                }

                FluidState fluidState = blockState.getFluidState();
                if (!fluidState.isEmpty() && !fluidState.is(Fluids.WATER)) {
                    return false;
                } else if (blockState.is(BlockTags.FIRE)) {
                    return false;
                } else {
                    return blockState.is(Blocks.SCULK_VEIN) || blockState.canBeReplaced() || super.stateCanBeReplaced(blockGetter, blockPos, blockPos2, direction, blockState);
                }
            } else {
                return false;
            }
        }

        public MultifaceSpreader.SpreadType @NotNull [] getSpreadTypes() {
            return this.spreadTypes;
        }

        public boolean isOtherBlockValidAsSource(BlockState blockState) {
            return !blockState.is(BlockRegistry.ASSIMILATED_SCULK_VEIN.get());
        }
    }
}
