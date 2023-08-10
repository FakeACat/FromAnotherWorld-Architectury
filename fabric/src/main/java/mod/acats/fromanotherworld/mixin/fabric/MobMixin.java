package mod.acats.fromanotherworld.mixin.fabric;

import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow @Final
    protected GoalSelector goalSelector;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci){
        if (level != null && !level.isClientSide()){
            CommonLivingEntityEvents.initGoals((Mob) (Object) this, this.goalSelector);
        }
    }
}
