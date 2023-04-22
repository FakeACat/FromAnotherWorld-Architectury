package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.AbstractThingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

public class ThingTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    protected final AbstractThingEntity mob;
    public ThingTargetGoal(AbstractThingEntity mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);
        this.mob = mob;
    }

    @Override
    protected double getFollowRange() {
        if (mob == null){
            return super.getFollowRange();
        }
        return mob.canHunt ? AbstractThingEntity.HUNTING_RANGE : super.getFollowRange();
    }

    @Override
    protected boolean canTrack(@Nullable LivingEntity target, TargetPredicate targetPredicate) {
        if (mob.canHunt){
            return target != null && mob.canTarget(target);
        }
        return super.canTrack(target, targetPredicate);
    }

    @Override
    protected Box getSearchBox(double distance) {
        if (mob.canHunt){
            return this.mob.getBoundingBox().expand(distance, distance / 2, distance);
        }
        return super.getSearchBox(distance);
    }

    protected void findClosestTarget() {
        if (mob.canHunt){
            LivingEntity livingEntity = this.mob.world.getClosestPlayer(this.mob, AbstractThingEntity.HUNTING_RANGE);
            if (livingEntity != null && mob.canTarget(livingEntity)){
                this.targetEntity = livingEntity;
            }
            return;
        }
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.targetEntity = this.mob.world.getClosestEntity(this.mob.world.getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), (livingEntity) -> true), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.targetEntity = this.mob.world.getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }
}
