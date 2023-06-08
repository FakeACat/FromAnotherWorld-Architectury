package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.StalkGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.interfaces.StalkerThing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SplitFaceEntity extends AbsorberThingEntity implements StalkerThing {
    public SplitFaceEntity(EntityType<? extends SplitFaceEntity> entityType, World world) {
        super(entityType, world, true);
        this.canGrief = true;
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.add(2, new ThingAttackGoal(this, 2.0D, false));
        this.goalSelector.add(3, new StalkGoal(this));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
    }

    public static DefaultAttributeContainer.Builder createSplitFaceAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FAWAnimations.defaultThing(this));
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.BLAIR_THING.get());
    }

    private PlayerEntity stalkTarget;

    @Override
    public PlayerEntity getStalkTarget() {
        return this.stalkTarget;
    }

    @Override
    public void setStalkTarget(PlayerEntity stalkTarget) {
        this.stalkTarget = stalkTarget;
    }
}
