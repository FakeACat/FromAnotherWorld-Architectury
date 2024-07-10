package mod.acats.fromanotherworld.fabric.events;

import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

public class ClientEntityLoadHandler implements ClientEntityEvents.Load {
    @Override
    public void onLoad(Entity entity, ClientLevel world) {
        if (entity instanceof LocalPlayer p) {
            CommonLivingEntityEvents.warnRedistribution(p);
        }
    }
}
