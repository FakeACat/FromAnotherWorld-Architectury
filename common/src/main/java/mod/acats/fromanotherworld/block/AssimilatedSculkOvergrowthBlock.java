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
public class AssimilatedSculkOvergrowthBlock extends AssimilatedSculkSpecialBlock {
    protected static final VoxelShape COLLIDER;
    public AssimilatedSculkOvergrowthBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_OVERGROWTH_BLOCK_ENTITY.get();
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return COLLIDER;
    }

    static {
        COLLIDER = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
