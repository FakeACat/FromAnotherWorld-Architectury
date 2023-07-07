package acats.fromanotherworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FlammableBlock extends Block {

    public FlammableBlock(Properties settings, int burn, int spread) {
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

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.burn();
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return true;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.spread();
    }
}
