package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class AssimilatedSculkActivatorBlock extends AssimilatedSculkSpecialBlock {
    protected static final VoxelShape SHAPE;
    public AssimilatedSculkActivatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_ACTIVATOR_BLOCK_ENTITY.get();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
