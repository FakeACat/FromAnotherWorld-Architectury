package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {

    @Inject(at =@At("HEAD"), method = "isHostile", cancellable = true)
    private void isHostile(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if (entity instanceof AbstractThingEntity){
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isCloseEnoughForDanger", cancellable = true)
    private void isCloseEnoughForDanger(LivingEntity villager, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (target instanceof AbstractThingEntity){
            cir.setReturnValue(target.squaredDistanceTo(villager) <= 144);
        }
    }
}
