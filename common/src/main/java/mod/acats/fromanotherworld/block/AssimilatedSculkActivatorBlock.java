package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AssimilatedSculkActivatorBlock extends AssimilatedSculkSpecialBlock {
    public AssimilatedSculkActivatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_ACTIVATOR_BLOCK_ENTITY.get();
    }
}
