package acats.fromanotherworld.spawning;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class AbstractThingEvent {
    public final ServerLevel world;
    public final ServerPlayer player;
    public AbstractThingEvent(ServerLevel world, ServerPlayer player){
        this.world = world;
        this.player = player;
    }
    public void run(){
        player.sendSystemMessage(Component.literal(this.warning()));
    }
    abstract String warning();
}
