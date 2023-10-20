package mod.acats.fromanotherworld.block.interfaces;

import mod.acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface AssimilatedSculk {
    static void revealNear(Level level, BlockPos blockPos){
        int range = 16;
        BlockUtilities.forEachBlockInCubeCentredAt(blockPos, range, pos -> {
            if (level.getRandom().nextInt((int)blockPos.distSqr(pos) / 25 + 1) == 0) {
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof AssimilatedSculk assimilatedSculk) {
                    assimilatedSculk.reveal(level, pos, state);
                }
            }
        });
    }
    void reveal(Level level, BlockPos pos, BlockState blockState);
}
