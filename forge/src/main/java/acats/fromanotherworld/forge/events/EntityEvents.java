package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.events.CommonEntityEvents;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent event){
        CommonEntityEvents.afterServerEntityDeath(event.getEntity(), event.getSource());
    }
}
