package acats.fromanotherworld.block;

import acats.fromanotherworld.block.interfaces.Gore;
import acats.fromanotherworld.constants.VariantID;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.memory.GlobalThingMemory;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class WallPalmerBlock extends FleshBlock implements Gore {
    public static final DirectionProperty FACING;
    public WallPalmerBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction facing = blockPlaceContext.getClickedFace();
        return facing.getAxis() != Direction.Axis.Y ? this.defaultBlockState().setValue(FACING, facing) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.isOnAcceptableSurface(world, pos, state.getValue(FACING).getOpposite());
    }

    private boolean isOnAcceptableSurface(BlockGetter getter, BlockPos pos, Direction side){
        BlockState blockState = getter.getBlockState(pos.relative(side));
        return blockState.isFaceSturdy(getter, pos.relative(side), side.getOpposite());
    }

    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public static BlockState facing(BlockState state, Direction direction){
        return state.setValue(FACING, direction);
    }

    @Override
    public boolean connectsHorizontally(BlockState blockState, Direction surface) {
        return blockState.getValue(FACING) == surface.getOpposite();
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        if (serverLevel.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 16, true) != null){
            this.activate(serverLevel, blockPos);
            return;
        }

        if (serverLevel.getRandom().nextInt(16) == 0) {
            GlobalThingMemory globalThingMemory = GlobalThingMemory.getGlobalThingMemory(serverLevel);
            globalThingMemory.closestBase(blockPos.getX(), blockPos.getY(), blockPos.getZ()).ifPresentOrElse(
                    thingBaseOfOperations -> {
                        if (thingBaseOfOperations.director.getHunger().activateHibernating) {
                            this.activate(serverLevel, blockPos);
                        }},
                    () -> this.activate(serverLevel, blockPos));
        }
    }

    private void activate(ServerLevel serverLevel, BlockPos blockPos) {
        serverLevel.destroyBlock(blockPos, false);
        Thing thing = EntityRegistry.PALMER_THING.get().create(serverLevel);
        if (thing != null) {
            thing.setVariantID(VariantID.GORE);
            thing.setPos(blockPos.getX() + 0.5D, blockPos.getY() - 1.0D, blockPos.getZ() + 0.5D);
            thing.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.NATURAL, null, null);
            serverLevel.addFreshEntity(thing);
        }
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
