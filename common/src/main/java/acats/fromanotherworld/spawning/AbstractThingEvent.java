package acats.fromanotherworld.spawning;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public abstract class AbstractThingEvent {
    public final ServerWorld world;
    public final ServerPlayerEntity player;
    public AbstractThingEvent(ServerWorld world, ServerPlayerEntity player){
        this.world = world;
        this.player = player;
    }
    public void run(){
        player.sendMessage(Text.literal(this.warning()));
    }
    abstract String warning();
}
