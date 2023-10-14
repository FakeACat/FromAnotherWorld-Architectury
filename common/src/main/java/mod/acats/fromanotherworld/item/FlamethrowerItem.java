package mod.acats.fromanotherworld.item;

import mod.acats.fromanotherlibrary.utilities.item.Aimable;
import mod.acats.fromanotherworld.entity.projectile.FlamethrowerFire;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FlamethrowerItem extends Item implements Aimable {
    private final float spread;
    private final float range;
    private final int frequency;
    public FlamethrowerItem(Properties properties, float spread, float range, int frequency) {
        super(properties);
        this.spread = spread;
        this.range = range;
        this.frequency = frequency;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        user.startUsingItem(hand);
        user.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (livingEntity.tickCount % frequency != 0) {
            return;
        }

        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            FlamethrowerFire fireball = new FlamethrowerFire(level, livingEntity);
            fireball.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, range, spread);
            level.addFreshEntity(fireball);
        }
    }

    @Override
    public boolean aiming(AbstractClientPlayer abstractClientPlayer, InteractionHand interactionHand) {
        return abstractClientPlayer.isUsingItem();
    }
}
