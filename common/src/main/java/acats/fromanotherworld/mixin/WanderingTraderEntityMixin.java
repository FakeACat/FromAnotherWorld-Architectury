package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import net.minecraft.entity.passive.WanderingTraderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin implements PossibleDisguisedThing {
    @Inject(at = @At("HEAD"), method = "tickDespawnDelay", cancellable = true)
    private void tickDespawnDelay(CallbackInfo ci){
        if (this.isAssimilated())
            ci.cancel();
    }
}
