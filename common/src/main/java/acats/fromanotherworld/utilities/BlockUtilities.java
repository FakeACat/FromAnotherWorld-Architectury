package acats.fromanotherworld.utilities;

import acats.fromanotherworld.block.TunnelBlock;
import acats.fromanotherworld.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

    public static void tryPlaceTunnelAt(Level level, BlockPos blockPos) {
        if ((level.getFluidState(blockPos).isEmpty() ||
                level.getFluidState(blockPos).is(Fluids.WATER)) &&
                (level.getBlockState(blockPos).canBeReplaced() || level.getBlockState(blockPos).is(BlockRegistry.TENTACLE.get())) &&
                level.getBlockState(blockPos.below()).isFaceSturdy(level, blockPos.below(), Direction.UP)) {
            level.setBlockAndUpdate(blockPos, BlockRegistry.TUNNEL_BLOCK.get().defaultBlockState()
                    .setValue(TunnelBlock.WATERLOGGED, level.getFluidState(blockPos).is(Fluids.WATER))
            );
        }
    }
}
