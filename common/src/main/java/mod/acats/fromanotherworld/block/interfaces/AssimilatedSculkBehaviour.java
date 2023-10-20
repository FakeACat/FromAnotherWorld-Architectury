package mod.acats.fromanotherworld.block.interfaces;

import mod.acats.fromanotherworld.block.AssimilatedSculkVeinBlock;
import mod.acats.fromanotherworld.block.spreading.AssimilatedSculkSpreader;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AssimilatedSculkBehaviour {
    AssimilatedSculkBehaviour DEFAULT = new AssimilatedSculkBehaviour() {
        public boolean attemptAssimilatedSpreadVein(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, @Nullable Collection<Direction> collection, boolean bl) {
            if (collection == null) {
                return BlockRegistry.ASSIMILATED_SCULK_VEIN.get().getSameSpaceSpreader().spreadAll(levelAccessor.getBlockState(blockPos), levelAccessor, blockPos, bl) > 0L;
            } else if (!collection.isEmpty()) {
                return (blockState.isAir() || blockState.getFluidState().is(Fluids.WATER)) && AssimilatedSculkVeinBlock.assimilatedRegrow(levelAccessor, blockPos, blockState, collection);
            } else {
                return AssimilatedSculkBehaviour.super.attemptAssimilatedSpreadVein(levelAccessor, blockPos, blockState, collection, bl);
            }
        }

        @Override
        public int attemptAssimilatedUseCharge(AssimilatedSculkSpreader.AssimilatedChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, AssimilatedSculkSpreader sculkSpreader, boolean bl) {
            return chargeCursor.getDecayDelay() > 0 ? chargeCursor.getCharge() : 0;
        }

        public int updateAssimilatedDecayDelay(int i) {
            return Math.max(i - 1, 0);
        }
    };

    default byte getAssimilatedSculkSpreadDelay() {
        return 1;
    }

    default void onAssimilatedDischarged(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, RandomSource randomSource) {
    }

    default boolean attemptAssimilatedSpreadVein(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, @Nullable Collection<Direction> collection, boolean bl) {
        return BlockRegistry.ASSIMILATED_SCULK_VEIN.get().getSpreader().spreadAll(blockState, levelAccessor, blockPos, bl) > 0L;
    }

    default boolean canAssimilatedChangeBlockStateOnSpread() {
        return true;
    }

    default int updateAssimilatedDecayDelay(int i) {
        return 1;
    }

    int attemptAssimilatedUseCharge(AssimilatedSculkSpreader.AssimilatedChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, AssimilatedSculkSpreader sculkSpreader, boolean bl);
}
