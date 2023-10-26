package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AssimilatedSculkOvergrowthBlock extends AssimilatedSculkSpecialBlock {
    public AssimilatedSculkOvergrowthBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_OVERGROWTH_BLOCK_ENTITY.get();
    }
}
