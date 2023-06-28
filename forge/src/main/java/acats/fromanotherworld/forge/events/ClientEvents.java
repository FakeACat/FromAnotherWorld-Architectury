package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.forge.registry.BlockEntityRegistryForge;
import acats.fromanotherworld.forge.registry.EntityRegistryForge;
import acats.fromanotherworld.forge.registry.ParticleRegistryForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
        BlockEntityRegistryForge.registerClient(event);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event){
        ParticleRegistryForge.clientRegister();
    }
}
