package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.resultant.AbstractMinibossThingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class MergeGoal extends Goal {
    private static final int RANGE = 16;
    private static final TargetPredicate VALID_MERGE_PREDICATE = TargetPredicate
            .createNonAttackable()
            .setPredicate((livingEntity) -> {
                if (livingEntity instanceof AbstractThingEntity thing)
                    return thing.canMerge() && !thing.mergeCore;
                return false;
            })
            .setBaseMaxDistance(RANGE)
            .ignoreVisibility();
    private final AbstractThingEntity thing;
    private final EntityType<? extends AbstractMinibossThingEntity> mergedForm;
    private AbstractThingEntity target;
    protected final World world;
    private int timer;

    public MergeGoal(AbstractThingEntity thing, EntityType<? extends AbstractMinibossThingEntity> mergedForm){
        this.thing = thing;
        this.mergedForm = mergedForm;
        this.world = thing.world;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!this.thing.mergeCore) {
            return false;
        } else {
            this.target = this.findMergeable();
            return this.target != null;
        }
    }
    @Override
    public boolean shouldContinue() {
        return this.target.isAlive() && this.target.canMerge() && !this.target.mergeCore && this.timer < 60;
    }
    @Override
    public void stop() {
        this.target = null;
        this.timer = 0;
    }

    @Override
    public void tick() {
        this.thing.getLookControl().lookAt(this.target, 10.0F, (float)this.thing.getMaxLookPitchChange());
        this.thing.getNavigation().startMovingTo(this.target, 1.0D);
        ++this.timer;
        if (this.timer >= this.getTickCount(60) && this.thing.squaredDistanceTo(this.target) < 9.0 && this.target.getMergeTimer() < 1) {
            this.merge();
        }
    }

    @Nullable
    private AbstractThingEntity findMergeable() {
        List<? extends AbstractThingEntity> list = this.world.getTargets(AbstractThingEntity.class, VALID_MERGE_PREDICATE, this.thing, this.thing.getBoundingBox().expand(RANGE));
        double d = Double.MAX_VALUE;
        AbstractThingEntity thingEntity = null;

        for (AbstractThingEntity thingEntity1 : list) {
            if (this.thing.squaredDistanceTo(thingEntity1) < d) {
                thingEntity = thingEntity1;
                d = this.thing.squaredDistanceTo(thingEntity1);
            }
        }

        return thingEntity;
    }

    protected void merge(){
        this.target.setMergeTimer(1);
        this.target.setMergeCoreID(this.thing.getId());
        this.thing.setMergedThings(this.thing.getMergedThings() + 1);
        if (this.thing.getMergedThings() > 2 && this.thing.getRandom().nextBoolean() || this.thing.getMergedThings() > 4){
            AbstractMinibossThingEntity miniboss = mergedForm.create(this.world);
            if (miniboss != null){
                miniboss.setPosition(this.thing.getPos());
                int tier = this.thing.getMergedThings() - 3;
                miniboss.setTier(tier, true);
                this.world.spawnEntity(miniboss);
                if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
                    miniboss.grief(0, 1);
            }

            this.thing.discard();
        }
    }
}
