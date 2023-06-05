package acats.fromanotherworld.mixin;

import acats.fromanotherworld.item.AssimilationLiquidItem;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends LivingEntity {

    @Shadow
    private int eatingTime;

    protected FoxEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    private void tickMovementMixin(CallbackInfo ci){
        if (!this.world.isClient && this.isAlive() && this.canMoveVoluntarily()) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (itemStack.getItem() instanceof AssimilationLiquidItem) {
                if (this.eatingTime > 600) {
                    itemStack.decrement(1);
                    EntityUtilities.assimilate(this);
                    this.eatingTime = 0;
                } else if (this.eatingTime > 560 && this.random.nextFloat() < 0.1F) {
                    this.playSound(this.getEatSound(itemStack), 1.0F, 1.0F);
                    this.world.sendEntityStatus(this, (byte)45);
                }
            }
        }
    }
}
