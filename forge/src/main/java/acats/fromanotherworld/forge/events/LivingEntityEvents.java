package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEntityEvents {
    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event){
        if (!event.getEntity().getWorld().isClient()){
            CommonLivingEntityEvents.serverEntityDeath(event.getEntity(), event.getSource());
            if (event.getEntity() instanceof PlayerEntity player)
                CommonLivingEntityEvents.serverPlayerEntityDeath(player, event.getSource());
        }
    }

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        CommonLivingEntityEvents.tick(entity);
        if (!entity.isRemoved())
            CommonLivingEntityEvents.tickMovement(entity);
    }

    @SubscribeEvent
    public static void livingChangeTarget(LivingChangeTargetEvent event){
        if (!CommonLivingEntityEvents.canTarget(event.getEntity(), event.getNewTarget()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event){
        CommonLivingEntityEvents.damage(event.getEntity(), event.getSource());
    }
}
