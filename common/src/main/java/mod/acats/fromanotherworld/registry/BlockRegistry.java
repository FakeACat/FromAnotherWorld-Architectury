package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.block.*;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final FALRegister<Block> BLOCK_REGISTRY = new FALRegister<>();

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    public static final FALRegistryObject<ThingGoreBlock> THING_GORE = registerToTab(
            "thing_gore",
            () -> new ThingGoreBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().randomTicks())
    );
    public static final FALRegistryObject<CorpseBlock> CORPSE = registerToTab(
            "corpse",
            () -> new CorpseBlock(BlockBehaviour.Properties.of().forceSolidOn().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FALRegistryObject<TentacleBlock> TENTACLE = registerToTab(
            "tentacle",
            () -> new TentacleBlock(BlockBehaviour.Properties.of().forceSolidOn().noCollission().noOcclusion().destroyTime(3.0F).randomTicks().ignitedByLava())
    );
    public static final FALRegistryObject<WallPalmerBlock> WALL_PALMER = registerToTab(
            "wall_palmer",
            () -> new WallPalmerBlock(BlockBehaviour.Properties.of().forceSolidOn().noCollission().noOcclusion().destroyTime(5.0F).randomTicks().ignitedByLava())
    );
    public static final FALRegistryObject<TunnelBlock> TUNNEL_BLOCK = registerToTab(
            "tunnel",
            () -> new TunnelBlock(BlockBehaviour.Properties.of().forceSolidOn().noCollission().noOcclusion().destroyTime(5.0F))
    );
    public static final FALRegistryObject<DisguisedTendrilsBlock> DISGUISED_TENDRILS = registerToTab(
            "disguised_tendrils",
            () -> new DisguisedTendrilsBlock(BlockBehaviour.Properties.of().forceSolidOn().noCollission().noOcclusion().instabreak().randomTicks().ignitedByLava())
    );

    public static final FALRegistryObject<AssimilatedSculkBlock> ASSIMILATED_SCULK = registerToTab(
            "assimilated_sculk",
            () -> new AssimilatedSculkBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks()
                    .mapColor(MapColor.COLOR_BLACK).strength(0.2F).sound(SoundType.SCULK))
    );

    public static final FALRegistryObject<AssimilatedSculkVeinBlock> ASSIMILATED_SCULK_VEIN = registerToTab(
            "assimilated_sculk_vein",
            () -> new AssimilatedSculkVeinBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks()
                    .mapColor(MapColor.COLOR_BLACK).forceSolidOn().noCollission().strength(0.2F).sound(SoundType.SCULK_VEIN).pushReaction(PushReaction.DESTROY))
    );

    public static final FALRegistryObject<AssimilatedSculkTentaclesBlock> ASSIMILATED_SCULK_TENTACLES = registerToTab(
            "assimilated_sculk_tentacles",
            () -> new AssimilatedSculkTentaclesBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks().forceSolidOn().noCollission().noOcclusion()
                    .mapColor(MapColor.COLOR_BLACK).strength(3.0F, 3.0F).sound(SoundType.SCULK_CATALYST).lightLevel((blockStatex) ->
                            blockStatex.getValue(AssimilatedSculk.REVEALED) ? 0 : 6
            ))
    );

    public static final FALRegistryObject<AssimilatedSculkActivatorBlock> ASSIMILATED_SCULK_ACTIVATOR = registerToTab(
            "assimilated_sculk_activator",
            () -> new AssimilatedSculkActivatorBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks().forceSolidOn().noCollission().noOcclusion()
                    .mapColor(MapColor.COLOR_CYAN).strength(1.5F).sound(SoundType.SCULK_SENSOR).lightLevel((blockStatex) ->
                            blockStatex.getValue(AssimilatedSculk.REVEALED) ? 0 : 1
            ))
    );

    public static final FALRegistryObject<AssimilatedSculkOvergrowthBlock> ASSIMILATED_SCULK_OVERGROWTH = registerToTab(
            "assimilated_sculk_overgrowth",
            () -> new AssimilatedSculkOvergrowthBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks().forceSolidOn().noCollission().noOcclusion()
                    .mapColor(MapColor.COLOR_BLACK).strength(3.0F, 3.0F).sound(SoundType.SCULK_SHRIEKER))
    );

    public static final FALRegistryObject<AssimilatedSculkBramblesBlock> ASSIMILATED_SCULK_BRAMBLES = registerToTab(
            "assimilated_sculk_brambles",
            () -> new AssimilatedSculkBramblesBlock(BlockBehaviour.Properties.of().isValidSpawn(BlockRegistry::never).randomTicks().forceSolidOn().noCollission().noOcclusion()
                    .mapColor(MapColor.COLOR_GRAY).strength(0.05F).sound(SoundType.GRASS).isViewBlocking(BlockRegistry::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(BlockRegistry::never))
    );

    private static <B extends Block> FALRegistryObject<B> registerToTab(String id, Supplier<B> supplier) {
        ItemRegistry.TAB_POPULATOR.setTabs(() -> ItemRegistry.ITEM_REGISTRY.get(id).orElseThrow(), ItemRegistry.TAB_KEY);
        return BLOCK_REGISTRY.register(id, supplier);
    }
}
