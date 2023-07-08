package acats.fromanotherworld.utilities;

import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class BlockUtilities {
    public static void forEachBlockInCubeCentredAt(BlockPos centre, int sideLength, Consumer<BlockPos> function){
        int size = (sideLength - 3) / 2;
        BlockPos.betweenClosedStream(
                centre.getX() - size,
                centre.getY() - size,
                centre.getZ() - size,
                centre.getX() + size,
                centre.getY() + size,
                centre.getZ() + size).forEach(function);
    }
}
