package acats.fromanotherworld.events;

import acats.fromanotherworld.block.ThingGore;
import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class CommonItemEvents {
    @Nullable
    public static ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand){
        if (!user.world.isClient() && stack.getItem() == Items.FLINT_AND_STEEL && entity instanceof AbstractThingEntity){
            entity.setOnFireFor(12);
            if (user instanceof ServerPlayerEntity) {
                stack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
            }
            return ActionResult.SUCCESS;
        }
        return null;
    }

    @Nullable
    public static ActionResult useOnBlock(ItemUsageContext context){
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
            return ActionResult.SUCCESS;
        }
        return null;
    }
}
