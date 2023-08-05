package acats.fromanotherworld.utilities.interfaces.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface CustomColourProviderBlock {
    int getDefaultColour();
    int getColour(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, int i);

    default ItemColor getItemColour() {
        return (itemStack, i) -> this.getDefaultColour();
    }
    default BlockColor getBlockColour() {
        return (blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockAndTintGetter == null || blockPos == null) {
                return this.getDefaultColour();
            }
            return this.getColour(blockState, blockAndTintGetter, blockPos, i);
        };
    }
}
