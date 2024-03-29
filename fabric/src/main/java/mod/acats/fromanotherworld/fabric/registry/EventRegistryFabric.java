package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.fabric.events.ServerLivingEntityDeathHandler;
import mod.acats.fromanotherworld.fabric.events.ServerWorldTickHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class EventRegistryFabric {
    public static void register(){
        ServerTickEvents.END_WORLD_TICK.register(new ServerWorldTickHandler());
        ServerLivingEntityEvents.AFTER_DEATH.register(new ServerLivingEntityDeathHandler());
    }
}
