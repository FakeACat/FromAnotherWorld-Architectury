package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistryFabric {
    public static void register(){
        BlockEntityRegistry.BLOCK_ENTITY_REGISTRY.registerAll(BlockEntityRegistryFabric::registerBlockEntity);
    }

    private static void registerBlockEntity(String id, FAWRegistryObject<? extends BlockEntityType<?>> fawRegistryObject){
        BlockEntityType<?> blockEntityType = fawRegistryObject.build();
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, id), blockEntityType);
    }

    public static void clientRegister(){
        BlockEntityRenderers.register(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new CorpseBlockEntityRenderer());
    }
}
