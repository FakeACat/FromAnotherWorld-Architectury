package mod.acats.fromanotherworld.forge.events;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.events.CommonItemEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractEvents {

    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event){
        if (event.getTarget() instanceof LivingEntity livingEntity){
            InteractionResult actionResult = CommonItemEvents.useOnEntity(event.getItemStack(), event.getEntity(), livingEntity, event.getHand());
            if (actionResult != null){
                event.setCanceled(true);
                event.setCancellationResult(actionResult);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event){
        UseOnContext context = new UseOnContext(event.getEntity(), event.getHand(), event.getHitVec());
        InteractionResult actionResult = CommonItemEvents.useOnBlock(context);
        if (actionResult != null){
            event.setCanceled(true);
            event.setCancellationResult(actionResult);
        }
    }
}
