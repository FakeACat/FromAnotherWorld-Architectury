package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.DamageTypeRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class AssimilatedSculkBramblesBlock extends Block {
    public AssimilatedSculkBramblesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        serverLevel.destroyBlock(blockPos, true);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);

        if (EntityUtilities.isThing(entity)) {
            return;
        }

        float damage = EntityUtilities.assimilate(entity, 0.05F) ? 0 : 0.1F;
        entity.hurt(DamageTypeRegistry.assimilation(entity.level()), damage);
        entity.invulnerableTime = 0;
    }
}
