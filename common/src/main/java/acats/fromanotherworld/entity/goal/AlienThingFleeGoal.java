package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.special.AlienThing;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class AlienThingFleeGoal extends AvoidEntityGoal<LivingEntity> {
    private final AlienThing mob;
    private final TargetingConditions withinRangePredicate;
    public AlienThingFleeGoal(AlienThing mob) {
        super(mob, LivingEntity.class, 50, 1.0D, 1.2D);
        this.mob = mob;
        this.withinRangePredicate = TargetingConditions.forCombat().range(this.maxDist).selector(predicateOnAvoidEntity.and(avoidPredicate));
    }

    @Override
    public boolean canUse() {
        if (EntityUtilities.isVulnerable(mob) || mob.getHealth() < mob.getMaxHealth() * 0.6F)
            this.mob.fleeing = true;
        return this.mob.fleeing && this.canStart2();
    }

    private boolean canStart2(){
        this.toAvoid = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate(this.maxDist, this.maxDist, this.maxDist), (livingEntity) -> true), this.withinRangePredicate, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        if (this.toAvoid == null) {
            return false;
        } else {
            Vec3 vec3d = DefaultRandomPos.getPosAway(this.mob, 12, 7, this.toAvoid.position());
            if (vec3d == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vec3d.x, vec3d.y, vec3d.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3d.x, vec3d.y, vec3d.z, 0);
                return this.path != null;
            }
        }
    }
}
