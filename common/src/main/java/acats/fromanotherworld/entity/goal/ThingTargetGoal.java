package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class ThingTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    protected final Thing mob;
    public ThingTargetGoal(Thing mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);
        this.mob = mob;
    }

    @Override
    protected double getFollowDistance() {
        if (mob == null){
            return super.getFollowDistance();
        }
        return mob.canHunt ? Thing.HUNTING_RANGE : super.getFollowDistance();
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity target, TargetingConditions targetPredicate) {
        if (mob.canHunt){
            return target != null && mob.canAttack(target);
        }
        return super.canAttack(target, targetPredicate);
    }

    @Override
    protected AABB getTargetSearchArea(double distance) {
        if (mob.canHunt){
            return this.mob.getBoundingBox().inflate(distance, distance / 2, distance);
        }
        return super.getTargetSearchArea(distance);
    }

    protected void findTarget() {
        if (mob.canHunt){
            LivingEntity livingEntity = this.mob.level().getNearestPlayer(this.mob, Thing.HUNTING_RANGE);
            if (livingEntity != null && mob.canAttack(livingEntity)){
                this.target = livingEntity;
            }
            return;
        }
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (livingEntity) -> true), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }
}
