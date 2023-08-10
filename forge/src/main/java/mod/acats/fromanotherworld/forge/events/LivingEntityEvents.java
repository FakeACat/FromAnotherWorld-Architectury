package mod.acats.fromanotherworld.forge.events;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEntityEvents {
    @SubscribeEvent
    public static void EntityJoinLevel(EntityJoinLevelEvent event){
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof Mob mob){
            CommonLivingEntityEvents.initGoals(mob, mob.goalSelector);
        }
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event){
        if (!event.getEntity().level().isClientSide()){
            CommonLivingEntityEvents.serverEntityDeath(event.getEntity(), event.getSource());
            if (event.getEntity() instanceof Player player)
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
