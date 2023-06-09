package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderEntityMixin implements PossibleDisguisedThing {
    @Inject(at = @At("HEAD"), method = "maybeDespawn", cancellable = true)
    private void maybeDespawn(CallbackInfo ci){
        if (this.isAssimilated())
            ci.cancel();
    }
}
