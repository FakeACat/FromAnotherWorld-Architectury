package mod.acats.fromanotherworld.block.interfaces;

import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.entity.misc.SculkRevealer;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;

public interface AssimilatedSculk {
    BooleanProperty REVEALED = BooleanProperty.create("revealed");
    int REDISGUISE_CHANCE = 3;
    static void alert(Level level, BlockPos blockPos, float strength){
        SculkRevealer.create(level, blockPos, (1.0F + level.getRandom().nextFloat()) * strength, 6, strength);
    }

    static void assimilateSurroundingSculk(Level level, BlockPos blockPos) {
        if (!Config.ASSIMILATED_SCULK_CONFIG.enabled.get()) {
            return;
        }

        BlockUtilities.forEachBlockInCubeCentredAt(blockPos, 1, pos -> {

            if (level.getRandom().nextInt(5) == 0) {
                BlockState currentState = level.getBlockState(pos);
                BlockState assimilatedState;

                if (currentState.is(Blocks.SCULK_VEIN)) {
                    assimilatedState = BlockRegistry.ASSIMILATED_SCULK_VEIN.get().defaultBlockState();
                    for (Map.Entry<Direction, BooleanProperty> entry:
                            PipeBlock.PROPERTY_BY_DIRECTION.entrySet()) {
                        BooleanProperty booleanProperty = entry.getValue();
                        assimilatedState = assimilatedState.setValue(booleanProperty, currentState.getValue(booleanProperty));
                    }
                } else if (currentState.is(Blocks.SCULK)) {
                    assimilatedState = BlockRegistry.ASSIMILATED_SCULK.get().disguisedBlockState();
                } else if (currentState.is(Blocks.SCULK_SENSOR)) {
                    assimilatedState = BlockRegistry.ASSIMILATED_SCULK_ACTIVATOR.get().disguisedBlockState();
                } else if (currentState.is(Blocks.SCULK_SHRIEKER)) {
                    assimilatedState = BlockRegistry.ASSIMILATED_SCULK_OVERGROWTH.get().disguisedBlockState();
                } else if (currentState.is(Blocks.SCULK_CATALYST)) {
                    assimilatedState = BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().disguisedBlockState();
                } else {
                    return;
                }

                level.setBlockAndUpdate(pos, assimilatedState);
            }
        });
    }

    void reveal(Level level, BlockPos pos, BlockState blockState, float strength);
    default boolean revealed(BlockState blockState) {
        return blockState.getValue(REVEALED);
    }
}
