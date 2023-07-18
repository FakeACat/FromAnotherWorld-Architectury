package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.fabric.events.ServerLivingEntityDeathHandler;
import acats.fromanotherworld.fabric.events.ServerWorldLoadHandler;
import acats.fromanotherworld.fabric.events.ServerWorldTickHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class EventRegistryFabric {
    public static void register(){
        ServerTickEvents.END_WORLD_TICK.register(new ServerWorldTickHandler());
        ServerLivingEntityEvents.AFTER_DEATH.register(new ServerLivingEntityDeathHandler());
        ServerWorldEvents.LOAD.register(new ServerWorldLoadHandler());
    }
}
