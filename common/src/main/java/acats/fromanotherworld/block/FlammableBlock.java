package acats.fromanotherworld.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public abstract class FlammableBlock extends Block {

    public FlammableBlock(Settings settings, int burn, int spread) {
        super(settings);
        this.burn = burn;
        this.spread = spread;
    }
    private final int burn;
    private final int spread;
    public int burn(){
        return this.burn;
    }
    public int spread(){
        return this.spread;
    }

    public int getFlammability(BlockState state, BlockView level, BlockPos pos, Direction direction)
    {
        return this.burn();
    }

    public boolean isFlammable(BlockState state, BlockView level, BlockPos pos, Direction direction)
    {
        return true;
    }

    public int getFireSpreadSpeed(BlockState state, BlockView level, BlockPos pos, Direction direction)
    {
        return this.spread();
    }
}
