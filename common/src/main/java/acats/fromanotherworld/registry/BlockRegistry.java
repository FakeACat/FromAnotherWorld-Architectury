package acats.fromanotherworld.registry;

import acats.fromanotherworld.block.*;

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
            () -> new TentacleBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(3.0F).randomTicks().ignitedByLava())
    );
    public static final FAWRegistryObject<WallPalmerBlock> WALL_PALMER = BLOCK_REGISTRY.register(
            "wall_palmer",
            () -> new WallPalmerBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F).randomTicks().ignitedByLava())
    );
    public static final FAWRegistryObject<TunnelBlock> TUNNEL_BLOCK = BLOCK_REGISTRY.register(
            "tunnel",
            () -> new TunnelBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FAWRegistryObject<DisguisedTendrilsBlock> DISGUISED_TENDRILS = BLOCK_REGISTRY.register(
            "disguised_tendrils",
            () -> new DisguisedTendrilsBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().randomTicks().ignitedByLava())
    );
}
