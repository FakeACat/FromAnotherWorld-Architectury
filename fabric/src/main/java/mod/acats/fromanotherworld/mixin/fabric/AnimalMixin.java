package mod.acats.fromanotherworld.mixin.fabric;

import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Animal.class)
public abstract class AnimalMixin {
    @Redirect(method = "spawnChildFromBreeding", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"))
    private AgeableMob modifyChild(Animal parent1, ServerLevel serverLevel, AgeableMob parent2) {
        AgeableMob child = parent1.getBreedOffspring(serverLevel, parent2);
        CommonLivingEntityEvents.onSpawnChild(parent1, parent2, child);
        return child;
    }
}
