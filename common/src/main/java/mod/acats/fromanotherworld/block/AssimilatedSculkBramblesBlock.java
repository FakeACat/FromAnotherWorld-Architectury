package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.DamageTypeRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class AssimilatedSculkBramblesBlock extends Block {
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public AssimilatedSculkBramblesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PERSISTENT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(PERSISTENT, true);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return super.isRandomlyTicking(blockState) && !blockState.getValue(PERSISTENT);
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
