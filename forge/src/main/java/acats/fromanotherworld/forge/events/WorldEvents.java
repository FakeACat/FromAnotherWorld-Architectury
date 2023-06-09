package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.events.CommonWorldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
    @SubscribeEvent
    public static void serverTickEvent(TickEvent.ServerTickEvent event){
        event.getServer().getAllLevels().forEach(WorldEvents::worldTick);
    }
    private static void worldTick(ServerLevel world){
        CommonWorldEvents.serverWorldTick(world);
    }
}
