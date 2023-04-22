package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
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
    public static void registerLayersEvent(EntityRenderersEvent.RegisterLayerDefinitions event){
        EntityRegistryForge.clientRegisterLayers(event);
    }

    @SubscribeEvent
    public static void registerRenderersEvent(EntityRenderersEvent.RegisterRenderers event){
        EntityRegistryForge.clientRegister(event);
    }

    @SubscribeEvent
    public static void registerParticleProvidersEvent(RegisterParticleProvidersEvent event){
        ParticleRegistryForge.clientRegister();
    }
}
