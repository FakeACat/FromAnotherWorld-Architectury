package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.thing.resultant.AbsorberThing;
import acats.fromanotherworld.utilities.EntityUtilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

public class AbsorbGoal extends Goal {
    private static final int RANGE = 10;
    private final TargetingConditions absorbPredicate;
    private final AbsorberThing absorber;
    private final Level world;
    private final int chance;
    private int timer = 0;
    public AbsorbGoal(AbsorberThing absorber, Predicate<LivingEntity> absorbable){
        this(absorber, absorbable, 800);
    }
    public AbsorbGoal(AbsorberThing absorber, Predicate<LivingEntity> absorbable, int chance){
        this.absorber = absorber;
        this.world = absorber.level();
        this.chance = chance;
        absorbPredicate = TargetingConditions
                .forNonCombat()
                .selector(absorbable)
                .range(RANGE);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
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
    public boolean canContinueToUse() {
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
            this.timer = 0;
            this.absorber.setAbsorbProgress(this.absorber.getAbsorbProgress() + 1);
            PossibleDisguisedThing target1 = (PossibleDisguisedThing) target;
            if (!target1.isAssimilated()){
                target1.setSupercellConcentration(target1.getSupercellConcentration() + 0.1F);
            }
            if (this.absorber.getAbsorbProgress() > AbsorberThing.ABSORB_TIME) {
                this.absorber.grow(target);
                target.discard();
                this.stop();
            }
        }
    }

    @Nullable
    private LivingEntity findAbsorbable() {
        List<? extends LivingEntity> list = this.world.getNearbyEntities(LivingEntity.class, absorbPredicate, this.absorber, this.absorber.getBoundingBox().inflate(RANGE));
        double d = Double.MAX_VALUE;
        LivingEntity entity = null;

        for (LivingEntity entity1 : list) {
            if (this.absorber.distanceToSqr(entity1) < d && EntityUtilities.canSee(this.absorber, entity1)) {
                entity = entity1;
                d = this.absorber.distanceToSqr(entity1);
            }
        }

        return entity;
    }
}
