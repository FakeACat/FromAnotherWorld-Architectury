package mod.acats.fromanotherworld.forge.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.utilities.registry.FAWRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistryForge {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FromAnotherWorld.MOD_ID);
    public static final FAWRegister<Item> FORGE_BLOCK_ITEMS = new FAWRegister<>();
    public static void register(IEventBus eventBus){
        BlockRegistry.BLOCK_REGISTRY.registerAll(BlockRegistryForge::registerBlockAndItem);
        BLOCKS.register(eventBus);
        FORGE_BLOCK_ITEMS.registerAll(ItemRegistryForge.ITEMS::register);
    }
    private static void registerBlockAndItem(String id, Supplier<? extends Block> supplier){
        RegistryObject<Block> block = BLOCKS.register(id, supplier);
        Supplier<Item> itemSupplier = () -> new BlockItem(block.get(), new Item.Properties());
        FORGE_BLOCK_ITEMS.register(id, itemSupplier);
    }
}
