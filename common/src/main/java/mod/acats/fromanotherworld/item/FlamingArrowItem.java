package mod.acats.fromanotherworld.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FlamingArrowItem extends ArrowItem {
    public FlamingArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        AbstractArrow arrow = super.createArrow(level, itemStack, livingEntity);
        arrow.setSecondsOnFire(100);
        return arrow;
    }
}
