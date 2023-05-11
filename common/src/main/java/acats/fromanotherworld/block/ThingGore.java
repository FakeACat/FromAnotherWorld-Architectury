package acats.fromanotherworld.block;


import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class ThingGore extends FlammableBlock {
    public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);
    protected static final VoxelShape SHAPE_FRESH = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape SHAPE_REFORMING = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);
    protected static final VoxelShape SHAPE_READY = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);
    public ThingGore(Settings settings) {
        super(settings, 15, 25);
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.WET_GRASS;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(STAGE)) {
            case 1 -> SHAPE_REFORMING;
            case 2 -> SHAPE_READY;
            default -> SHAPE_FRESH;
        };
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isFullCube(world, pos.down());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient()){
            int stage = state.get(STAGE);
            if (stage >= 2){
                world.breakBlock(pos, false);
                BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(world);
                if (bloodCrawlerEntity != null) {
                    bloodCrawlerEntity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    world.spawnEntity(bloodCrawlerEntity);
                }
            }
            else{
                world.setBlockState(pos, state.with(STAGE, stage + 1));
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}
