package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.fabric.event.ServerLivingEntityDeathHandler;
import acats.fromanotherworld.fabric.event.ServerWorldTickHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class EventRegistryFabric {
    public static void register(){
        ServerTickEvents.END_WORLD_TICK.register(new ServerWorldTickHandler());
        ServerLivingEntityEvents.AFTER_DEATH.register(new ServerLivingEntityDeathHandler());
    }
}
