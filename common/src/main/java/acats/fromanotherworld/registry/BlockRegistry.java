package acats.fromanotherworld.registry;

import acats.fromanotherworld.block.CorpseBlock;
import acats.fromanotherworld.block.TentacleBlock;
import acats.fromanotherworld.block.ThingGoreBlock;

import acats.fromanotherworld.block.WallPalmerBlock;
import acats.fromanotherworld.utilities.registry.FAWRegister;
import acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRegistry {
    public static final FAWRegister<Block> BLOCK_REGISTRY = new FAWRegister<>();

    public static final FAWRegistryObject<ThingGoreBlock> THING_GORE = BLOCK_REGISTRY.register(
            "thing_gore",
            () -> new ThingGoreBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().randomTicks())
    );
    public static final FAWRegistryObject<CorpseBlock> CORPSE = BLOCK_REGISTRY.register(
            "corpse",
            () -> new CorpseBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FAWRegistryObject<TentacleBlock> TENTACLE = BLOCK_REGISTRY.register(
            "tentacle",
            () -> new TentacleBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(3.0F).randomTicks())
    );
    public static final FAWRegistryObject<WallPalmerBlock> WALL_PALMER = BLOCK_REGISTRY.register(
            "wall_palmer",
            () -> new WallPalmerBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F).randomTicks())
    );
}
