package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.forge.registry.BlockRegistryForge;
import acats.fromanotherworld.forge.registry.ItemRegistryForge;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InventoryEvents {
    @SubscribeEvent
    public static void buildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent event){
        if (event.getTab() == ItemRegistryForge.TAB.get()){
            ItemRegistry.ITEM_REGISTRY.forEach((id, fawItem) -> event.accept(fawItem.get()));
            ItemRegistry.SPAWN_EGG_REGISTRY.forEach((id, fawEgg) -> event.accept(ItemRegistryForge.FORGE_SPAWN_EGGS.get(id)));
            BlockRegistryForge.FORGE_BLOCK_ITEMS.forEach((id, fawItem) -> event.accept(fawItem.get()));
        }
    }
}
