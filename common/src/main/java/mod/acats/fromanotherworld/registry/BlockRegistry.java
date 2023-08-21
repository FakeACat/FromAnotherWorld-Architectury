package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final FALRegister<Block> BLOCK_REGISTRY = new FALRegister<>();

    public static final FALRegistryObject<ThingGoreBlock> THING_GORE = registerToTab(
            "thing_gore",
            () -> new ThingGoreBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().randomTicks())
    );
    public static final FALRegistryObject<CorpseBlock> CORPSE = registerToTab(
            "corpse",
            () -> new CorpseBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FALRegistryObject<TentacleBlock> TENTACLE = registerToTab(
            "tentacle",
            () -> new TentacleBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(3.0F).randomTicks().ignitedByLava())
    );
    public static final FALRegistryObject<WallPalmerBlock> WALL_PALMER = registerToTab(
            "wall_palmer",
            () -> new WallPalmerBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F).randomTicks().ignitedByLava())
    );
    public static final FALRegistryObject<TunnelBlock> TUNNEL_BLOCK = registerToTab(
            "tunnel",
            () -> new TunnelBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FALRegistryObject<DisguisedTendrilsBlock> DISGUISED_TENDRILS = registerToTab(
            "disguised_tendrils",
            () -> new DisguisedTendrilsBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().randomTicks().ignitedByLava())
    );

    private static <B extends Block> FALRegistryObject<B> registerToTab(String id, Supplier<B> supplier) {
        ItemRegistry.TAB_POPULATOR.setTabs(() -> ItemRegistry.ITEM_REGISTRY.get(id).orElseThrow(), ItemRegistry.TAB_KEY);
        return BLOCK_REGISTRY.register(id, supplier);
    }
}
