package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.fabric.events.ClientEntityLoadHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;

public class ClientEventRegistryFabric {
    public static void register(){
        ClientEntityEvents.ENTITY_LOAD.register(new ClientEntityLoadHandler());
    }
}
