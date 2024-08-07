package mod.acats.fromanotherworld.forge.events;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import mod.acats.fromanotherworld.events.CommonWorldEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
    @SubscribeEvent
    public static void levelTickEvent(TickEvent.LevelTickEvent event){
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            CommonWorldEvents.serverWorldTick(serverLevel);
        }
    }

    @SubscribeEvent
    public static void entityJoinLevelEvent(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LocalPlayer p) {
            CommonLivingEntityEvents.warnRedistribution(p);
        }
    }
}
