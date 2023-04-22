package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.forge.registry.BlockRegistryForge;
import acats.fromanotherworld.forge.registry.ItemRegistryForge;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InventoryEvents {
    @SubscribeEvent
    public static void creativeModeTabRegisterEvent(CreativeModeTabEvent.Register event){
        ItemRegistry.modItemGroup = event.registerCreativeModeTab(new Identifier(FromAnotherWorld.MOD_ID, "from_another_world_group"), InventoryEvents::buildModTab);
    }
    private static void buildModTab(ItemGroup.Builder builder){
        builder.displayName(Text.literal("From Another World"));
        builder.icon(() -> new ItemStack(ItemRegistry.ASSIMILATION_LIQUID.get()));
        builder.entries((displayContext, entries) -> {
            ItemRegistry.ITEM_REGISTRY.forEach((id, fawItem) -> entries.add(fawItem.get()));
            ItemRegistry.SPAWN_EGG_REGISTRY.forEach((id, fawEgg) -> entries.add(ItemRegistryForge.FORGE_SPAWN_EGGS.get(id)));
            BlockRegistryForge.FORGE_BLOCK_ITEMS.forEach((id, fawItem) -> entries.add(fawItem.get()));
        });
        builder.build();
    }
}
