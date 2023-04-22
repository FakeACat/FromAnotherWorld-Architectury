package acats.fromanotherworld.item;

import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AssimilationLiquidItem extends Item {
    public AssimilationLiquidItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, user);
            assimilationLiquid.setItem(itemStack);
            assimilationLiquid.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0F);
            world.spawnEntity(assimilationLiquid);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
