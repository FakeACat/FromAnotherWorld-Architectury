package acats.fromanotherworld.mixin;

import acats.fromanotherworld.block.ThingGore;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(at = @At("HEAD"), method = "useOnEntity", cancellable = true)
    private void useFlintAndSteelOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if (!user.world.isClient() && stack.getItem() == Items.FLINT_AND_STEEL && entity instanceof AbstractThingEntity){
            entity.setOnFireFor(12);
            if (user instanceof ServerPlayerEntity) {
                stack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    private void useBottleOnGore(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        if (context.getStack().getItem() == Items.GLASS_BOTTLE && context.getPlayer() != null && context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof ThingGore){
            if (context.getWorld().isClient()){
                context.getWorld().playSound(context.getPlayer(), context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.BLOCK_WET_GRASS_STEP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            else{
                context.getWorld().removeBlock(context.getBlockPos(), false);
                if (!context.getPlayer().isCreative())
                    context.getStack().decrement(1);
                if (!context.getPlayer().getInventory().insertStack(new ItemStack(ItemRegistry.GORE_BOTTLE.get())))
                    context.getPlayer().dropItem(new ItemStack(ItemRegistry.GORE_BOTTLE.get()), false);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
