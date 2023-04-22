package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class FleeOnFireGoal extends FleeEntityGoal<LivingEntity> {
    public FleeOnFireGoal(PathAwareEntity mob, float distance, double slowSpeed, double fastSpeed) {
        super(mob, LivingEntity.class, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canStart() {
        return FromAnotherWorld.isVulnerable(mob) && super.canStart();
    }
}
