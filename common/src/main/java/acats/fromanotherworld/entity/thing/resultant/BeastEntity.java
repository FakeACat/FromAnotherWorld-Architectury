package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.BeastAttackGoal;
import acats.fromanotherworld.entity.goal.ThingProjectileAttackGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BeastEntity extends MinibossThingEntity implements RangedAttackMob {

    private static final TrackedData<Boolean> MELEE;

    public BeastEntity(EntityType<? extends BeastEntity> entityType, World world) {
        super(entityType, world);
        this.canGrief = true;
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.add(2, new BeastAttackGoal(this, 1.0D, false));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, LivingEntity.class, 10.0F, 1.0, 1.2, (livingEntity) -> livingEntity.equals(this.getTarget())));
        this.goalSelector.add(4, new ThingProjectileAttackGoal(this, 1.0, 20, 20, 16.0F));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -1.5F;
    }

    @Override
    double getStartingHealth() {
        return 120.0D;
    }

    @Override
    double getScalingHealth() {
        return 60.0D;
    }

    @Override
    double getStartingSpeed() {
        return 0.35D;
    }

    @Override
    double getScalingSpeed() {
        return 0.05D;
    }

    @Override
    double getStartingDamage() {
        return 10.0D;
    }

    @Override
    double getScalingDamage() {
        return 5.0D;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(MELEE, false);
        super.initDataTracker();
    }

    private void toggleMelee(){
        this.dataTracker.set(MELEE, !this.isMelee());
    }

    public boolean isMelee(){
        return this.dataTracker.get(MELEE);
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if (this.age % 300 == 0)
            this.toggleMelee();
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (event.isMoving() || this.movingClimbing()){
            event.getController().setAnimationSpeed(2.0D);
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.beast.run"));
        }
        else{
            if (this.isMelee())
                return PlayState.STOP;
            else{
                event.getController().setAnimationSpeed(1.0D);
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.beast.spit"));
            }
        }
        return PlayState.CONTINUE;
    }
    private <E extends GeoEntity> PlayState predicateTentacles(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        event.getController().setAnimationSpeed(2.0D);
        if (this.isMelee()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.beast.tentacles_out").thenPlay("animation.beast.tentacles"));
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.beast.tentacles_in"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "tentacleController", 0, this::predicateTentacles));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("IsMelee", this.isMelee());
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("IsMelee") && nbt.getBoolean("IsMelee")){
            this.toggleMelee();
        }
        super.readCustomDataFromNbt(nbt);
    }

    public static DefaultAttributeContainer.Builder createBeastAttributes(){
        return createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48);
    }

    static {
        MELEE = DataTracker.registerData(BeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        for (int i = 0; i < 4; i++){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, this);
            assimilationLiquid.setPos(this.getX(), this.getBodyY(0.5D), this.getZ());
            assimilationLiquid.setVelocity(target.getPos().add(0, target.getHeight() / 2, 0).subtract(assimilationLiquid.getPos()).normalize().multiply(1.75D).add(new Vec3d(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).multiply(0.01)));
            world.spawnEntity(assimilationLiquid);
        }
    }
}
