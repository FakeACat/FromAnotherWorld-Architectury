package mod.acats.fromanotherworld.entity.thing.resultant;

import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.entity.goal.AbsorbGoal;
import mod.acats.fromanotherworld.entity.goal.DirectedWanderGoal;
import mod.acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import mod.acats.fromanotherworld.entity.goal.LeapAttackGoal;
import mod.acats.fromanotherworld.entity.interfaces.Leaper;
import mod.acats.fromanotherworld.entity.thing.ResizeableThing;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DogBeast extends ResizeableThing implements Leaper {

    private static final EntityDataAccessor<Boolean> LEAPING;

    public DogBeast(EntityType<? extends DogBeast> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.0, 1.2));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD, Config.DIFFICULTY_CONFIG.dogBeastMergeChance.get()));
        this.goalSelector.addGoal(2, new LeapAttackGoal(this, 1.0D, false, 120, 1.5D, 0.5D, 5.0D));
        this.goalSelector.addGoal(3, new DirectedWanderGoal(this, 1.0D));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEAPING, false);
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
        controllerRegistrar.add(FAWAnimations.defaultThingNoChaseWithLeap(this));
    }

    @Override
    public double animationSpeed(AnimationState<? extends Thing> state) {
        return state.isMoving() ? super.animationSpeed(state) * 2.0D : super.animationSpeed(state);
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.STANDARD;
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

    @Override
    public boolean isLeaping() {
        return this.entityData.get(LEAPING);
    }

    @Override
    public void setLeaping(boolean leaping) {
        this.entityData.set(LEAPING, leaping);
    }

    static {
        LEAPING = SynchedEntityData.defineId(DogBeast.class, EntityDataSerializers.BOOLEAN);
    }
}
