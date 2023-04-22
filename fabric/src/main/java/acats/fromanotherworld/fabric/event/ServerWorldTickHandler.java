package acats.fromanotherworld.fabric.event;

import acats.fromanotherworld.events.CommonWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

public class ServerWorldTickHandler implements ServerTickEvents.EndWorldTick{
    @Override
    public void onEndTick(ServerWorld world) {
        CommonWorldEvents.serverWorldTick(world);
    }
}
