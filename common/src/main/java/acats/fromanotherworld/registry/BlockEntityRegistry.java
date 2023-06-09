package acats.fromanotherworld.registry;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import acats.fromanotherworld.utilities.registry.FAWRegister;
import acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistry {
    public static final FAWRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = new FAWRegister<>();

    public static final FAWRegistryObject<BlockEntityType<CorpseBlockEntity>> CORPSE_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register("corpse_block_entity", () -> BlockEntityType.Builder.of(CorpseBlockEntity::new, BlockRegistry.CORPSE.get()).build(null));
}
