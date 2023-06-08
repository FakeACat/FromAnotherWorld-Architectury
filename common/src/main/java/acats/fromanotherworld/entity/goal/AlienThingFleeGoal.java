package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.util.math.Vec3d;

public class AlienThingFleeGoal extends FleeEntityGoal<LivingEntity> {
    private final AlienThingEntity mob;
    private final TargetPredicate withinRangePredicate;
    public AlienThingFleeGoal(AlienThingEntity mob) {
        super(mob, LivingEntity.class, 50, 1.0D, 1.2D);
        this.mob = mob;
        this.withinRangePredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.fleeDistance).setPredicate(inclusionSelector.and(extraInclusionSelector));
    }

    @Override
    public boolean canStart() {
        if (EntityUtilities.isVulnerable(mob) || mob.getHealth() < mob.getMaxHealth() * 0.6F)
            this.mob.fleeing = true;
        return this.mob.fleeing && this.canStart2();
    }

    private boolean canStart2(){
        this.targetEntity = this.mob.getWorld().getClosestEntity(this.mob.getWorld().getEntitiesByClass(this.classToFleeFrom, this.mob.getBoundingBox().expand(this.fleeDistance, this.fleeDistance, this.fleeDistance), (livingEntity) -> true), this.withinRangePredicate, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        if (this.targetEntity == null) {
            return false;
        } else {
            Vec3d vec3d = NoPenaltyTargeting.findFrom(this.mob, 12, 7, this.targetEntity.getPos());
            if (vec3d == null) {
                return false;
            } else if (this.targetEntity.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z) < this.targetEntity.squaredDistanceTo(this.mob)) {
                return false;
            } else {
                this.fleePath = this.fleeingEntityNavigation.findPathTo(vec3d.x, vec3d.y, vec3d.z, 0);
                return this.fleePath != null;
            }
        }
    }
}
