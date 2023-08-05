package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.block.FlammableBlock;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.utilities.interfaces.block.CustomColourProviderBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
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

import java.util.function.Supplier;

public class BlockRegistryFabric {
    public static void register(){
        BlockRegistry.BLOCK_REGISTRY.registerAll(BlockRegistryFabric::registerBlockAndItem);
    }

    private static void registerBlockAndItem(String id, Supplier<? extends Block> blockSupplier){
        Block block = blockSupplier.get();
        Item item = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(FromAnotherWorld.MOD_ID, id), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(ItemRegistryFabric.GROUP_KEY).register(entries -> entries.accept(item));
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(FromAnotherWorld.MOD_ID, id), block);

        if (block instanceof FlammableBlock flammable){
            FlammableBlockRegistry.getDefaultInstance().add(block, flammable.burn(), flammable.spread());
        }

        if (block instanceof CustomColourProviderBlock colourProvider) {
            ColorProviderRegistry.BLOCK.register(colourProvider.getBlockColour(), BlockRegistry.DISGUISED_TENDRILS.get());
            ColorProviderRegistry.ITEM.register(colourProvider.getItemColour(), item);
        }
    }

    public static void registerClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.TENTACLE.get(), RenderType.cutout()); // note to self - forge does not handle this with code, must be done in the model json
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.DISGUISED_TENDRILS.get(), RenderType.cutout());
    }
}
