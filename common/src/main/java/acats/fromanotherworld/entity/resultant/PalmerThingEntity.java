package acats.fromanotherworld.entity.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.MergeGoal;
import acats.fromanotherworld.entity.goal.PalmerAttackGoal;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class PalmerThingEntity extends AbstractThingEntity {

    private static final TrackedData<Integer> TARGET_ID;

    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public PalmerThingEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new PalmerAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new MergeGoal(this, EntityRegistry.BLAIR_THING.get()));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TARGET_ID, 0);
    }

    public void setTargetId(int id){
        this.dataTracker.set(TARGET_ID, id);
    }

    public int getTargetId(){
        return this.dataTracker.get(TARGET_ID);
    }

    public boolean targetGrabbed(){
        if (this.getTargetId() != 0){
            Entity target = this.world.getEntityById(this.getTargetId());
            if (target != null){
                if (FromAnotherWorld.isThing(target)){
                    this.setTargetId(0);
                    return false;
                }
                return target.squaredDistanceTo(this) < 25;
            }
        }
        return false;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.age % 20 == 0 && this.getTarget() != null){
            this.setTargetId(this.getTarget().getId());
            if (this.targetGrabbed()){
                this.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1), this);
            }
        }
    }

    public static DefaultAttributeContainer.Builder createPalmerThingAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.325D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "headController", 10, this::predicateHead));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (event.isMoving() && !this.isThingFrozen()){
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.palmer_thing.run"));
        }
        else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoEntity> PlayState predicateHead(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (this.targetGrabbed()){
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.palmer_thing.tentacle"));
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.palmer_thing.head_idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD_STRONG;
    }

    static {
        TARGET_ID = DataTracker.registerData(PalmerThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
