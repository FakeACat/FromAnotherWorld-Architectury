package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class SplitFaceEntity extends AbstractAbsorberThingEntity {
    public SplitFaceEntity(EntityType<? extends SplitFaceEntity> entityType, World world) {
        super(entityType, world, true);
        this.canGrief = true;
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new AbsorbGoal(this,
                STANDARD,
                (livingEntity) -> defaultGrow(livingEntity, EntityRegistry.BLAIR_THING.get())
        ));
        this.goalSelector.add(2, new ThingAttackGoal(this, 1.25D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75D));
    }

    public static DefaultAttributeContainer.Builder createSplitFaceAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32);
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STRONG;
    }
}
