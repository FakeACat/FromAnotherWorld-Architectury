package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.events.CommonWorldEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
    @SubscribeEvent
    public static void serverTickEvent(TickEvent.ServerTickEvent event){
        event.getServer().getWorlds().forEach(WorldEvents::worldTick);
    }
    private static void worldTick(ServerWorld world){
        CommonWorldEvents.serverWorldTick(world);
    }
}
