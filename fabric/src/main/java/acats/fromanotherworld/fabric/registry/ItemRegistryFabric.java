package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.ItemRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegistryFabric {
    public static final RegistryKey<ItemGroup> GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(FromAnotherWorld.MOD_ID, ItemRegistry.TAB_ID));
    public static void register(){
        Registry.register(Registries.ITEM_GROUP, GROUP_KEY, FabricItemGroup.builder()
                .displayName(Text.translatable(FromAnotherWorld.MOD_ID + "." + ItemRegistry.TAB_ID))
                .icon(() -> new ItemStack(ItemRegistry.ASSIMILATION_LIQUID.get()))
                .build());
        ItemRegistry.ITEM_REGISTRY.forEach(ItemRegistryFabric::registerItem);
        ItemRegistry.SPAWN_EGG_REGISTRY.forEach(ItemRegistryFabric::registerSpawnEgg);
    }

    private static void registerSpawnEgg(String id, ItemRegistry.FAWEgg fawEgg){
        registerItem(id, new ItemRegistry.FAWItem(() -> new SpawnEggItem(fawEgg.entityTypeSupplier.get(), fawEgg.primaryColour, fawEgg.secondaryColour, new Item.Settings())));
    }

    private static void registerItem(String id, ItemRegistry.FAWItem fawItem){
        Item item = Registry.register(Registries.ITEM, new Identifier(FromAnotherWorld.MOD_ID, id), fawItem.build());
        ItemGroupEvents.modifyEntriesEvent(GROUP_KEY).register(entries -> entries.add(item));
    }
}
