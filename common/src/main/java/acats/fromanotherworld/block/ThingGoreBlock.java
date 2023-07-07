package acats.fromanotherworld.block;


import acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ThingGoreBlock extends FleshBlock {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    protected static final VoxelShape SHAPE_FRESH = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape SHAPE_REFORMING = Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);
    protected static final VoxelShape SHAPE_READY = Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);
    public ThingGoreBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(STAGE)) {
            case 1 -> SHAPE_REFORMING;
            case 2 -> SHAPE_READY;
            default -> SHAPE_FRESH;
        };
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isClientSide()){
            int stage = state.getValue(STAGE);
            if (stage >= 2){
                world.destroyBlock(pos, false);
                BloodCrawler bloodCrawler = EntityRegistry.BLOOD_CRAWLER.get().create(world);
                if (bloodCrawler != null) {
                    bloodCrawler.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    world.addFreshEntity(bloodCrawler);
                }
            }
            else{
                world.setBlockAndUpdate(pos, state.setValue(STAGE, stage + 1));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}
