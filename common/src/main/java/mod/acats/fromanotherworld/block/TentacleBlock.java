package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.block.interfaces.Gore;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@SuppressWarnings("deprecation")
public class TentacleBlock extends FleshBlock implements Gore {
    protected static final VoxelShape SHAPE_N = box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SHAPE_E = box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SHAPE_S = box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SHAPE_W = box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape SHAPE_D = box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
    protected static final VoxelShape SHAPE_U = box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

    public static final EnumProperty<TentacleSide> NORTH;
    public static final EnumProperty<TentacleSide> EAST;
    public static final EnumProperty<TentacleSide> SOUTH;
    public static final EnumProperty<TentacleSide> WEST;
    public static final EnumProperty<TentacleSide> ABOVE;
    public static final EnumProperty<TentacleSide> BELOW;
    public static final DirectionProperty SURFACE;

    public TentacleBlock(Properties settings) {
        super(settings);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, ABOVE, BELOW, SURFACE);
    }
    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        blockState = blockState.setValue(SURFACE, rotation.rotate(blockState.getValue(SURFACE)));
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90 ->
                    blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default -> blockState;
        };
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        blockState = blockState.rotate(mirror.getRotation(blockState.getValue(SURFACE)));
        return switch (mirror) {
            case LEFT_RIGHT ->
                    blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK ->
                    blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default -> super.mirror(blockState, mirror);
        };
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(SURFACE)){
            case NORTH -> SHAPE_N;
            case EAST -> SHAPE_E;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            case UP -> SHAPE_U;
            case DOWN -> SHAPE_D;
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return BlockUtilities.isOnAcceptableSurface(world, pos, state.getValue(SURFACE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return correctConnectionStates(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), this.defaultBlockState().setValue(SURFACE, blockPlaceContext.getClickedFace().getOpposite()));
    }

    public static BlockState correctConnectionStates(Level level, BlockPos pos, BlockState initialState){
        Direction surface = initialState.getValue(SURFACE);
        Direction up = surface.getOpposite();

        initialState = initialState.setValue(NORTH, updateSide(level, pos.north(), surface, up));
        initialState = initialState.setValue(EAST, updateSide(level, pos.east(), surface, up));
        initialState = initialState.setValue(SOUTH, updateSide(level, pos.south(), surface, up));
        initialState = initialState.setValue(WEST, updateSide(level, pos.west(), surface, up));
        initialState = initialState.setValue(ABOVE, updateSide(level, pos.above(), surface, up));
        initialState = initialState.setValue(BELOW, updateSide(level, pos.below(), surface, up));

        return initialState;
    }

    private static TentacleSide updateSide(Level level, BlockPos pos, Direction surface, Direction up){
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof Gore gore && gore.connectsHorizontally(state, surface)){
            return TentacleSide.CONNECTED;
        }
        else if (level.getBlockState(pos.relative(up)).getBlock() instanceof Gore gore &&
                gore.connectsHorizontally(level.getBlockState(pos.relative(up)), surface)){
            return TentacleSide.CONNECTED_UP;
        }
        else if (state.isCollisionShapeFullBlock(level, pos)){
            return TentacleSide.BLOCK;
        }
        else{
            return TentacleSide.NONE;
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState2.is(blockState.getBlock()) && !level.isClientSide) {

            for (Direction direction : Direction.Plane.VERTICAL) {
                level.updateNeighborsAt(blockPos.relative(direction), this);
            }

            this.updateNeighborsOfNeighboringWires(level, blockPos);
        }
    }

    private void updateNeighborsOfNeighboringWires(Level level, BlockPos blockPos) {
        Iterator<Direction> var3 = Direction.Plane.HORIZONTAL.iterator();

        Direction direction;
        while(var3.hasNext()) {
            direction = var3.next();
            this.checkCornerChangeAt(level, blockPos.relative(direction));
        }
    }

    private void checkCornerChangeAt(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).is(this)) {
            level.updateNeighborsAt(blockPos, this);
            Direction[] var3 = Direction.values();

            for (Direction direction : var3) {
                level.updateNeighborsAt(blockPos.relative(direction), this);
            }

        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide() && level.getBlockState(blockPos) == blockState){
            level.setBlockAndUpdate(blockPos, correctConnectionStates(level, blockPos, blockState));
        }
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide()){
            Direction[] var6 = Direction.values();
            for (Direction direction : var6) {
                level.updateNeighborsAt(blockPos.relative(direction), this);
            }
            this.updateNeighborsOfNeighboringWires(level, blockPos);
        }
    }

    @Override
    public boolean connectsHorizontally(BlockState blockState, Direction surface) {
        return blockState.getValue(SURFACE) == surface;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);

        if (!serverLevel.isClientSide()){
            if (serverLevel.getRandom().nextInt(3) == 0){
                this.spread(serverLevel, blockPos, blockState);
            }

            if (blockState.getValue(SURFACE) == Direction.DOWN && serverLevel.getRandom().nextInt(5) == 0){
                int assimilables = EntityUtilities.numAssimilablesInList(EntityUtilities.nearbyEntities(serverLevel, new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 8, 4));
                if (assimilables > 0){
                    this.spawnThingFromGore(serverLevel, blockPos);
                }
            }
        }
    }

    private void spawnThingFromGore(ServerLevel level, BlockPos pos){
        Thing thing = EntityRegistry.BLOOD_CRAWLER.get().create(level);
        if (thing != null) {
            thing.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            thing.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
            level.addFreshEntity(thing);
        }
    }

    @Override
    public Direction surface(Level level, BlockState blockState) {
        return blockState.getValue(SURFACE);
    }

    public enum TentacleSide implements StringRepresentable {
        NONE("none"),
        BLOCK("block"),
        CONNECTED_UP("connected_up"),
        CONNECTED("connected");

        TentacleSide(String name) {
            this.name = name;
        }
        private final String name;
        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    static {
        NORTH = EnumProperty.create("north", TentacleSide.class);
        EAST = EnumProperty.create("east", TentacleSide.class);
        SOUTH = EnumProperty.create("south", TentacleSide.class);
        WEST = EnumProperty.create("west", TentacleSide.class);
        ABOVE = EnumProperty.create("above", TentacleSide.class);
        BELOW = EnumProperty.create("below", TentacleSide.class);
        SURFACE = DirectionProperty.create("surface");
    }
}
