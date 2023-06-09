package acats.fromanotherworld.mixin;

import acats.fromanotherworld.item.AssimilationLiquidItem;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fox.class)
public abstract class FoxEntityMixin extends LivingEntity {

    @Shadow
    private int ticksSinceEaten;

    protected FoxEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "aiStep")
    private void aiStepMixin(CallbackInfo ci){
        if (!this.level().isClientSide && this.isAlive() && this.isEffectiveAi()) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.getItem() instanceof AssimilationLiquidItem) {
                if (this.ticksSinceEaten > 600) {
                    itemStack.shrink(1);
                    EntityUtilities.assimilate(this);
                    this.ticksSinceEaten = 0;
                } else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1F) {
                    this.playSound(this.getEatingSound(itemStack), 1.0F, 1.0F);
                    this.level().broadcastEntityEvent(this, (byte)45);
                }
            }
        }
    }
}
