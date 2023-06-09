package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.block.FlammableBlock;
import acats.fromanotherworld.registry.BlockRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockRegistryFabric {
    public static void register(){
        BlockRegistry.BLOCK_REGISTRY.forEach(BlockRegistryFabric::registerBlockAndItem);
    }

    public static void clientRegister(){
        BlockRegistry.BLOCK_REGISTRY.forEach((id, fawBlock) -> registerRenderer(fawBlock));
    }

    private static void registerBlockAndItem(String id, BlockRegistry.FAWBlock fawBlock){
        Block block = fawBlock.build();
        Item item = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(FromAnotherWorld.MOD_ID, id), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(ItemRegistryFabric.GROUP_KEY).register(entries -> entries.accept(item));
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(FromAnotherWorld.MOD_ID, id), block);

        if (block instanceof FlammableBlock flammable){
            FlammableBlockRegistry.getDefaultInstance().add(block, flammable.burn(), flammable.spread());
        }
    }
    private static void registerRenderer(BlockRegistry.FAWBlock fawBlock){
        BlockRenderLayerMap.INSTANCE.putBlock(fawBlock.get(), RenderType.cutout());
    }
}
