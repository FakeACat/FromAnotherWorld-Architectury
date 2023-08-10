package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRegistryFabric {
    public static void register(){
        BlockEntityRegistry.BLOCK_ENTITY_REGISTRY.registerAll(BlockEntityRegistryFabric::registerBlockEntity);
    }

    private static void registerBlockEntity(String id, Supplier<? extends BlockEntityType<?>> builder){
        BlockEntityType<?> blockEntityType = builder.get();
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, id), blockEntityType);
    }
}
