package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TraderLlama.class)
public abstract class TraderLlamaEntityMixin implements PossibleDisguisedThing {
    @Inject(at = @At("TAIL"), method = "canDespawn", cancellable = true)
    private void canDespawn(CallbackInfoReturnable<Boolean> cir){
        if (this.isAssimilated())
            cir.setReturnValue(false);
    }
}
