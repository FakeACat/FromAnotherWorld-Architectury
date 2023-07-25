package acats.fromanotherworld.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BlockUtilities {
    public static void forEachBlockInCubeCentredAt(BlockPos centre, int radius, Consumer<BlockPos> function){
        BlockPos.betweenClosedStream(
                centre.getX() - radius,
                centre.getY() - radius,
                centre.getZ() - radius,
                centre.getX() + radius,
                centre.getY() + radius,
                centre.getZ() + radius).forEach(function);
    }

    public static void grief(Level level, int size, int x, int y, int z, @Nullable Entity entity, int chanceDenominator) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }

        for(int o = -size; o <= size; ++o) {
            for(int p = -size; p <= size; ++p) {
                for(int q = 0; q <= size + 2; ++q) {
                    int r = x + o;
                    int s = y + q;
                    int t = z + p;
                    BlockPos blockPos = new BlockPos(r, s, t);
                    BlockState blockState = level.getBlockState(blockPos);
                    if (EntityUtilities.canThingDestroy(blockState) && level.getRandom().nextInt(chanceDenominator) == 0) {
                        level.destroyBlock(blockPos, false, entity);
                    }
                }
            }
        }
    }
}
