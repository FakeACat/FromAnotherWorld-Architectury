package acats.fromanotherworld.mixin.fabric;

import acats.fromanotherworld.events.CommonItemEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(at = @At("HEAD"), method = "useOnEntity", cancellable = true)
    private void useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ActionResult actionResult = CommonItemEvents.useOnEntity(stack, user, entity, hand);
        if (actionResult != null)
            cir.setReturnValue(actionResult);
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        ActionResult actionResult = CommonItemEvents.useOnBlock(context);
        if (actionResult != null)
            cir.setReturnValue(actionResult);
    }
}
