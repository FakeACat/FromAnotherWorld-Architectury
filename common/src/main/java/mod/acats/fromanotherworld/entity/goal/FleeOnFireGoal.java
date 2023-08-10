package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeOnFireGoal extends AvoidEntityGoal<LivingEntity> {
    public FleeOnFireGoal(PathfinderMob mob, float distance, double slowSpeed, double fastSpeed) {
        super(mob, LivingEntity.class, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canUse() {
        return EntityUtilities.isVulnerable(mob) && super.canUse();
    }
}
