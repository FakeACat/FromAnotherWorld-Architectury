package acats.fromanotherworld.events;

import acats.fromanotherworld.block.ThingGoreBlock;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;

public class CommonItemEvents {
    @Nullable
    public static InteractionResult useOnEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand){
        if (!user.level().isClientSide() && stack.getItem() == Items.FLINT_AND_STEEL && entity instanceof Thing){
            entity.setSecondsOnFire(12);
            if (user instanceof ServerPlayer) {
                stack.hurtAndBreak(1, user, (p) -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    @Nullable
    public static InteractionResult useOnBlock(UseOnContext context){
        if (context.getItemInHand().getItem() == Items.GLASS_BOTTLE && context.getPlayer() != null && context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof ThingGoreBlock){
            if (context.getLevel().isClientSide()){
                context.getLevel().playSound(context.getPlayer(), context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.WET_GRASS_STEP, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            else{
                context.getLevel().removeBlock(context.getClickedPos(), false);
                if (!context.getPlayer().isCreative())
                    context.getItemInHand().shrink(1);
                if (!context.getPlayer().getInventory().add(new ItemStack(ItemRegistry.GORE_BOTTLE.get())))
                    context.getPlayer().drop(new ItemStack(ItemRegistry.GORE_BOTTLE.get()), false);
            }
            return InteractionResult.SUCCESS;
        }
        return null;
    }
}
