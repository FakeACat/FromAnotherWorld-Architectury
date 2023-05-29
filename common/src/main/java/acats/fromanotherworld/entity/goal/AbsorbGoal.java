package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.thing.resultant.AbsorberThingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class AbsorbGoal extends Goal {
    private static final int RANGE = 16;
    private final TargetPredicate absorbPredicate;
    private final AbsorberThingEntity absorber;
    private final World world;
    private final int chance;
    private int timer = 0;
    public AbsorbGoal(AbsorberThingEntity absorber, Predicate<LivingEntity> absorbable){
        this(absorber, absorbable, 300);
    }
    public AbsorbGoal(AbsorberThingEntity absorber, Predicate<LivingEntity> absorbable, int chance){
        this.absorber = absorber;
        this.world = absorber.getWorld();
        this.chance = chance;
        absorbPredicate = TargetPredicate
                .createNonAttackable()
                .setPredicate(absorbable)
                .setBaseMaxDistance(RANGE)
                .ignoreVisibility();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (this.absorber.cannotMerge() || this.world.getRandom().nextInt(chance) != 0)
            return false;
        LivingEntity absorbTarget = this.findAbsorbable();
        if (absorbTarget != null){
            this.absorber.setAbsorbTarget(absorbTarget);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity absorbTarget = this.absorber.getAbsorbTarget();
        return this.timer < 300 && absorbTarget != null && absorbTarget.isAlive();
    }

    @Override
    public void stop() {
        this.absorber.setAbsorbTargetID(0);
        this.absorber.setAbsorbProgress(0);
        this.timer = 0;
    }

    @Override
    public void tick() {
        timer++;
        LivingEntity target = this.absorber.getAbsorbTarget();
        if (target != null){
            this.absorber.getNavigation().startMovingTo(target, 1.0D);
            if (this.absorber.squaredDistanceTo(target) < 64.0) {
                this.timer = 0;
                this.absorber.setAbsorbProgress(this.absorber.getAbsorbProgress() + 1);
                PossibleDisguisedThing target1 = (PossibleDisguisedThing) target;
                if (!target1.isAssimilated()){
                    target1.setSupercellConcentration(target1.getSupercellConcentration() + 0.1F);
                }
                if (this.absorber.getAbsorbProgress() > AbsorberThingEntity.ABSORB_TIME) {
                    this.absorber.grow(target);
                    target.discard();
                    this.stop();
                }
            }
        }
    }

    @Nullable
    private LivingEntity findAbsorbable() {
        List<? extends LivingEntity> list = this.world.getTargets(LivingEntity.class, absorbPredicate, this.absorber, this.absorber.getBoundingBox().expand(RANGE));
        double d = Double.MAX_VALUE;
        LivingEntity entity = null;

        for (LivingEntity entity1 : list) {
            if (this.absorber.squaredDistanceTo(entity1) < d) {
                entity = entity1;
                d = this.absorber.squaredDistanceTo(entity1);
            }
        }

        return entity;
    }
}
