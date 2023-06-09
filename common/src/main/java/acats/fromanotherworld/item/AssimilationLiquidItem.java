package acats.fromanotherworld.item;

import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AssimilationLiquidItem extends Item {
    public AssimilationLiquidItem(Properties settings) {
        super(settings);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!world.isClientSide) {
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, user);
            assimilationLiquid.setItem(itemStack);
            assimilationLiquid.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 0F);
            world.addFreshEntity(assimilationLiquid);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.isCreative()) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
