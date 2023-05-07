package acats.fromanotherworld.mixin.fabric;

import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixinFabric {
    private LivingEntity getEntity(){
        return (LivingEntity) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource damageSource, CallbackInfo ci){
        CommonLivingEntityEvents.serverEntityDeath(this.getEntity(), damageSource);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        CommonLivingEntityEvents.tick(this.getEntity());
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    private void tickMovement(CallbackInfo ci){
        CommonLivingEntityEvents.tickMovement(this.getEntity());
    }

    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (!CommonLivingEntityEvents.canTarget(this.getEntity(), target))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        CommonLivingEntityEvents.damage(this.getEntity(), source);
    }
}
