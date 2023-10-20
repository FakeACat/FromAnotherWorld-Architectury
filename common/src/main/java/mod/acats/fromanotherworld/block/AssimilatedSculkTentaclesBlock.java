package mod.acats.fromanotherworld.block;

import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AssimilatedSculkTentaclesBlock extends AssimilatedSculkSpecialBlock {
    public AssimilatedSculkTentaclesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.ASSIMILATED_SCULK_TENTACLES_BLOCK_ENTITY.get();
    }
}
