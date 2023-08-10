package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class ItemRegistryFabric {
    public static final ResourceKey<CreativeModeTab> GROUP_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(FromAnotherWorld.MOD_ID, ItemRegistry.TAB_ID));
    public static void register(){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, GROUP_KEY, FabricItemGroup.builder()
                .title(Component.translatable(FromAnotherWorld.MOD_ID + "." + ItemRegistry.TAB_ID))
                .icon(() -> new ItemStack(ItemRegistry.ASSIMILATION_LIQUID.get()))
                .build());
        ItemRegistry.ITEM_REGISTRY.registerAll(ItemRegistryFabric::registerItem);
        ItemRegistry.SPAWN_EGG_REGISTRY.forEach(ItemRegistryFabric::registerSpawnEgg);
    }

    private static void registerSpawnEgg(String id, ItemRegistry.FAWEgg fawEgg){
        registerItem(id, () -> new SpawnEggItem(fawEgg.entityTypeSupplier.get(), fawEgg.primaryColour, fawEgg.secondaryColour, new Item.Properties()));
    }

    private static void registerItem(String id, Supplier<? extends Item> supplier){
        Item item = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(FromAnotherWorld.MOD_ID, id), supplier.get());
        ItemGroupEvents.modifyEntriesEvent(GROUP_KEY).register(entries -> entries.accept(item));
    }
}
