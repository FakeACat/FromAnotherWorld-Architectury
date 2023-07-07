package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class BlockRegistryForge {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FromAnotherWorld.MOD_ID);
    public static final HashMap<String, ItemRegistry.FAWItem> FORGE_BLOCK_ITEMS = new HashMap<>();
    public static void register(IEventBus eventBus){
        BlockRegistry.BLOCK_REGISTRY.registerAll(BlockRegistryForge::registerBlockAndItem);
        BLOCKS.register(eventBus);
    }
    private static void registerBlockAndItem(String id, Supplier<? extends Block> supplier){
        RegistryObject<Block> block = BLOCKS.register(id, supplier);
        ItemRegistry.FAWItem fawItem = new ItemRegistry.FAWItem(() -> new BlockItem(block.get(), new Item.Properties()));
        FORGE_BLOCK_ITEMS.put(id, fawItem);
        ItemRegistryForge.ITEMS.register(id, fawItem::build);
    }
}
