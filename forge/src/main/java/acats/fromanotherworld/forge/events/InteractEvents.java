package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.events.CommonItemEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractEvents {

    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event){
        if (event.getTarget() instanceof LivingEntity livingEntity){
            ActionResult actionResult = CommonItemEvents.useOnEntity(event.getItemStack(), event.getEntity(), livingEntity, event.getHand());
            if (actionResult != null){
                event.setCanceled(true);
                event.setCancellationResult(actionResult);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event){
        ItemUsageContext context = new ItemUsageContext(event.getEntity(), event.getHand(), event.getHitVec());
        ActionResult actionResult = CommonItemEvents.useOnBlock(context);
        if (actionResult != null){
            event.setCanceled(true);
            event.setCancellationResult(actionResult);
        }
    }
}
