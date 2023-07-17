package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockEntityRegistryForge {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FromAnotherWorld.MOD_ID);
    public static void register(IEventBus eventBus){
        BlockEntityRegistry.BLOCK_ENTITY_REGISTRY.registerAll(BlockEntityRegistryForge::registerBlockEntity);
        BLOCK_ENTITIES.register(eventBus);
    }
    private static void registerBlockEntity(String id, Supplier<? extends BlockEntityType<?>> builder){
        BLOCK_ENTITIES.register(id, builder);
    }
}
