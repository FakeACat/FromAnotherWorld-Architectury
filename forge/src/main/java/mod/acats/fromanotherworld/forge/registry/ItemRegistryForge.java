package mod.acats.fromanotherworld.forge.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class ItemRegistryForge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FromAnotherWorld.MOD_ID);
    public static final HashMap<String, ForgeSpawnEggItem> FORGE_SPAWN_EGGS = new HashMap<>();
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FromAnotherWorld.MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB =
            TABS.register(ItemRegistry.TAB_ID, () -> CreativeModeTab.builder()
                    .title(Component.translatable(FromAnotherWorld.MOD_ID + "." + ItemRegistry.TAB_ID))
                    .icon(() -> new ItemStack(ItemRegistry.ASSIMILATION_LIQUID.get()))
                    .build());
    public static void register(IEventBus eventBus){
        ItemRegistry.ITEM_REGISTRY.registerAll(ItemRegistryForge::registerItem);
        ItemRegistry.SPAWN_EGG_REGISTRY.forEach(ItemRegistryForge::registerSpawnEgg);
        ITEMS.register(eventBus);
        TABS.register(eventBus);
    }

    private static void registerItem(String id, Supplier<? extends Item> itemSupplier){
        ITEMS.register(id, itemSupplier);
    }
    private static void registerSpawnEgg(String id, ItemRegistry.FAWEgg fawEgg){
        ITEMS.register(id, () -> {
            ForgeSpawnEggItem spawnEgg = new ForgeSpawnEggItem(fawEgg.entityTypeSupplier, fawEgg.primaryColour, fawEgg.secondaryColour, new Item.Properties());
            FORGE_SPAWN_EGGS.put(id, spawnEgg);
            return spawnEgg;
        });
    }
}
