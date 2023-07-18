package acats.fromanotherworld.fabric.events;

import acats.fromanotherworld.events.CommonWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ServerWorldLoadHandler implements ServerWorldEvents.Load{
    @Override
    public void onWorldLoad(MinecraftServer server, ServerLevel world) {
        CommonWorldEvents.serverLevelLoad(world);
    }
}
