package acats.fromanotherworld.mixin.fabric;

import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
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

    @Inject(at = @At("HEAD"), method = "die")
    private void die(DamageSource damageSource, CallbackInfo ci){
        CommonLivingEntityEvents.serverEntityDeath(this.getEntity(), damageSource);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        CommonLivingEntityEvents.tick(this.getEntity());
    }

    @Inject(at = @At("HEAD"), method = "aiStep")
    private void aiStep(CallbackInfo ci){
        CommonLivingEntityEvents.tickMovement(this.getEntity());
    }

    @Inject(at = @At("HEAD"), method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", cancellable = true)
    private void canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (!CommonLivingEntityEvents.canTarget(this.getEntity(), target))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "hurt")
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        CommonLivingEntityEvents.damage(this.getEntity(), source);
    }
}
