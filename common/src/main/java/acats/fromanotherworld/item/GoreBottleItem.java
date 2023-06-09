package acats.fromanotherworld.item;

import acats.fromanotherworld.registry.DamageTypeRegistry;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class GoreBottleItem extends Item {
    public GoreBottleItem(Properties settings) {
        super(settings);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player)user : null;
        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (playerEntity != null) {
            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.getAbilities().instabuild) {
                stack.shrink(1);
            }
            playerEntity.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

            if (!world.isClientSide() && playerEntity.canBeSeenAsEnemy()){
                EntityUtilities.spawnAssimilatedPlayer(playerEntity);
                playerEntity.hurt(DamageTypeRegistry.amongUsPotion(world), Float.MAX_VALUE);
            }
        }

        user.gameEvent(GameEvent.DRINK);

        return super.finishUsingItem(stack, world, user);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
}
