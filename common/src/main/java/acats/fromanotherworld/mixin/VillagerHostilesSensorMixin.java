package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.thing.ThingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {

    @Inject(at =@At("HEAD"), method = "isHostile", cancellable = true)
    private void isHostile(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if (entity instanceof ThingEntity){
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isClose", cancellable = true)
    private void isClose(LivingEntity villager, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (target instanceof ThingEntity){
            cir.setReturnValue(target.distanceToSqr(villager) <= 144);
        }
    }
}
