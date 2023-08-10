package mod.acats.fromanotherworld.forge.events;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.forge.registry.BlockRegistryForge;
import mod.acats.fromanotherworld.forge.registry.EntityRegistryForge;
import mod.acats.fromanotherworld.forge.registry.ParticleRegistryForge;
import mod.acats.fromanotherworld.forge.registry.client.BlockEntityRegistryClientForge;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.utilities.interfaces.block.CustomColourProviderBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        EntityRegistryForge.clientRegisterLayers(event);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        EntityRegistryForge.clientRegister(event);
        BlockEntityRegistryClientForge.registerClient(event);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event){
        ParticleRegistryForge.clientRegister();
    }

    @SubscribeEvent
    public static void registerColorHandlersItemEvent(RegisterColorHandlersEvent.Item event) {
        BlockRegistryForge.FORGE_BLOCK_ITEMS.forEach((id, itemSupplier) -> {
            Item item = itemSupplier.get();
            Block block = BlockRegistry.BLOCK_REGISTRY.get(id);
            if (block instanceof CustomColourProviderBlock colourProvider) {
                event.register(colourProvider.getItemColour(), item);
            }
        });
    }

    @SubscribeEvent
    public static void registerColorHandlersBlockEvent(RegisterColorHandlersEvent.Block event) {
        BlockRegistry.BLOCK_REGISTRY.forEach((id, blockSupplier) -> {
            Block block = blockSupplier.get();
            if (block instanceof CustomColourProviderBlock colourProvider) {
                event.register(colourProvider.getBlockColour(), BlockRegistry.DISGUISED_TENDRILS.get());
            }
        });
    }
}
