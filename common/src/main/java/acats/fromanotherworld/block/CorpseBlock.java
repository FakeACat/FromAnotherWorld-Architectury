package acats.fromanotherworld.block;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CorpseBlock extends BaseEntityBlock {
    public static final EnumProperty<CorpseType> TYPE = EnumProperty.create("type", CorpseType.class);

    public CorpseBlock(Properties properties) {
        super(properties);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState state) {
        return SoundType.WET_GRASS;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CorpseBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static CorpseType getCorpseType(BlockState state){
        return state.getValue(TYPE);
    }
    public static void setCorpseType(BlockState state, CorpseType corpseType){
        state.setValue(TYPE, corpseType);
    }
    public enum CorpseType implements StringRepresentable {
        HUMAN_1("human_1", 1),
        SMALL_1("small_1", 2);

        private final int size;
        private final String name;
        CorpseType(String name, int size){
            this.name = name;
            this.size = size;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
