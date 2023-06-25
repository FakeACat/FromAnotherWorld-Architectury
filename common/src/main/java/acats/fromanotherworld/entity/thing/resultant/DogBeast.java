package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.thing.ResizeableThing;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DogBeast extends ResizeableThing {

    public DogBeast(EntityType<? extends DogBeast> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.0, 1.2));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(2, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public float tentacleOriginOffset() {
        return this.getEyeHeight();
    }

    public static AttributeSupplier.Builder createDogBeastAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.MAX_HEALTH, 30.0D);
    }

    @Override
    public void die(DamageSource source) {
        if (random.nextInt(3) == 0){
            DogBeastSpitter dogSpitterEntity = EntityRegistry.DOGBEAST_SPITTER.get().create(this.level());
            if (dogSpitterEntity != null) {
                dogSpitterEntity.setPos(this.position());
                dogSpitterEntity.initializeFrom(this);
                this.level().addFreshEntity(dogSpitterEntity);
            }
        }
        super.die(source);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(FAWAnimations.defaultThingNoChase(this));
    }

    @Override
    public double animationSpeed(AnimationState<? extends Thing> state) {
        return state.isMoving() ? super.animationSpeed(state) * 2.0D : super.animationSpeed(state);
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD;
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
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.PROWLER.get());
    }
}
