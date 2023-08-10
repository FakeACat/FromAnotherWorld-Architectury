package mod.acats.fromanotherworld.fabric.events;

import mod.acats.fromanotherworld.events.CommonWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;

public class ServerWorldTickHandler implements ServerTickEvents.EndWorldTick{
    @Override
    public void onEndTick(ServerLevel world) {
        CommonWorldEvents.serverWorldTick(world);
    }
}
