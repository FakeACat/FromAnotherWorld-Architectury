package mod.acats.fromanotherworld.mixin.fabric;

import mod.acats.fromanotherworld.events.CommonItemEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(at = @At("HEAD"), method = "interactLivingEntity", cancellable = true)
    private void interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        InteractionResult actionResult = CommonItemEvents.useOnEntity(stack, user, entity, hand);
        if (actionResult != null)
            cir.setReturnValue(actionResult);
    }

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir){
        InteractionResult actionResult = CommonItemEvents.useOnBlock(context);
        if (actionResult != null)
            cir.setReturnValue(actionResult);
    }
}
