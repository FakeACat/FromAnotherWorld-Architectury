package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.entity.AssimilatedSculkTentaclesBlockEntity;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AssimilatedSculkTentaclesBlock extends AssimilatedSculkSpecialBlock {
    public static final int MAX_LOCAL_ACTIVE_TENTACLES = 1;
    public static final int CHECK_ZONE_SIZE = 48;
    public AssimilatedSculkTentaclesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_TENTACLES_BLOCK_ENTITY.get();
    }

    @Override
    protected boolean canRedisguiseRandomly() {
        return false;
    }

    @Override
    public void reveal(Level level, BlockPos pos, BlockState blockState, float strength) {
        long count = level.getBlockStates(AABB.ofSize(Vec3.atCenterOf(pos), CHECK_ZONE_SIZE, CHECK_ZONE_SIZE, CHECK_ZONE_SIZE)).filter(this::filter).count();
        if (count < MAX_LOCAL_ACTIVE_TENTACLES) {
            super.reveal(level, pos, blockState, strength);
        }

        if (level.getBlockEntity(pos) instanceof AssimilatedSculkTentaclesBlockEntity tentaclesBlockEntity) {
            tentaclesBlockEntity.activeTime = AssimilatedSculkTentaclesBlockEntity.MAX_ACTIVE_TIME;
        }
    }

    private boolean filter(BlockState blockState) {
        return blockState.is(BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get()) && blockState.getValue(REVEALED);
    }
}
