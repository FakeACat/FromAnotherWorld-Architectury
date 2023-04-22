package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class ItemRegistryForge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FromAnotherWorld.MOD_ID);
    public static final HashMap<String, ForgeSpawnEggItem> FORGE_SPAWN_EGGS = new HashMap<>();
    public static void register(IEventBus eventBus){
        ItemRegistry.ITEM_REGISTRY.forEach(ItemRegistryForge::registerItem);
        ItemRegistry.SPAWN_EGG_REGISTRY.forEach(ItemRegistryForge::registerSpawnEgg);
        ITEMS.register(eventBus);
    }

    private static void registerItem(String id, ItemRegistry.FAWItem fawItem){
        ITEMS.register(id, fawItem::build);
    }
    private static void registerSpawnEgg(String id, ItemRegistry.FAWEgg fawEgg){
        ITEMS.register(id, () -> {
            ForgeSpawnEggItem spawnEgg = new ForgeSpawnEggItem(fawEgg.entityTypeSupplier, fawEgg.primaryColour, fawEgg.secondaryColour, new Item.Settings());
            FORGE_SPAWN_EGGS.put(id, spawnEgg);
            return spawnEgg;
        });
    }
}
