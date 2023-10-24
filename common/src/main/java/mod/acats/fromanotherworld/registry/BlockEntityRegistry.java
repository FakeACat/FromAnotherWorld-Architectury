package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkActivatorBlockEntity;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkTentaclesBlockEntity;
import mod.acats.fromanotherworld.block.entity.CorpseBlockEntity;
import mod.acats.fromanotherworld.block.entity.TunnelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistry {
    public static final FALRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = new FALRegister<>();

    public static final FALRegistryObject<BlockEntityType<CorpseBlockEntity>> CORPSE_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
            "corpse_block_entity",
            () -> BlockEntityType.Builder.of(
                    CorpseBlockEntity::new,
                    BlockRegistry.CORPSE.get()
            ).build(null));

    public static final FALRegistryObject<BlockEntityType<TunnelBlockEntity>> TUNNEL_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
            "tunnel_block_entity",
            () -> BlockEntityType.Builder.of(
                    TunnelBlockEntity::new,
                    BlockRegistry.TUNNEL_BLOCK.get()
            ).build(null));

    public static final FALRegistryObject<BlockEntityType<AssimilatedSculkTentaclesBlockEntity>> ASSIMILATED_SCULK_TENTACLES_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
            "assimilated_sculk_tentacles",
            () -> BlockEntityType.Builder.of(
                    AssimilatedSculkTentaclesBlockEntity::new,
                    BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get()
            ).build(null));

    public static final FALRegistryObject<BlockEntityType<AssimilatedSculkActivatorBlockEntity>> ASSIMILATED_SCULK_ACTIVATOR_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
            "assimilated_sculk_activator",
            () -> BlockEntityType.Builder.of(
                    AssimilatedSculkActivatorBlockEntity::new,
                    BlockRegistry.ASSIMILATED_SCULK_ACTIVATOR.get()
            ).build(null));
}
