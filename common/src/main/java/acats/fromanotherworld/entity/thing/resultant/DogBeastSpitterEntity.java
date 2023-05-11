package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.MergeGoal;
import acats.fromanotherworld.entity.goal.ThingProjectileAttackGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DogBeastSpitterEntity extends AbstractThingEntity implements RangedAttackMob {

    private static final TrackedData<Boolean> ATTACKING;

    private final AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);

    public DogBeastSpitterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKING, false);
    }

    public void setHasTarget(boolean bl){
        this.dataTracker.set(ATTACKING, bl);
    }

    public boolean hasTarget(){
        return this.dataTracker.get(ATTACKING);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.setHasTarget(this.getTarget() != null);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new ThingProjectileAttackGoal(this, 1.0, 40, 80, 10.0F));
        this.goalSelector.add(2, new MergeGoal(this, EntityRegistry.BEAST.get()));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (event.isMoving() || this.movingClimbing()){
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dogbeast_spitter.crawl"));
        }
        else if (this.hasTarget()){
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dogbeast_spitter.spit"));
        }
        else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    public static DefaultAttributeContainer.Builder createDogBeastSpitterAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 22.0D);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        for (int i = 0; i < (this.canSpit ? 6 : 3); i++){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, this);
            assimilationLiquid.setVelocity(target.getPos().add(0, target.getHeight() / 2, 0).subtract(assimilationLiquid.getPos()).normalize().add(new Vec3d(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).multiply(this.canSpit ? 0.02 : 0.01)));
            world.spawnEntity(assimilationLiquid);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    static {
        ATTACKING = DataTracker.registerData(DogBeastSpitterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD_WEAK;
    }
}
