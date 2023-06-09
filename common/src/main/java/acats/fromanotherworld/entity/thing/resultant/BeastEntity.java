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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BeastEntity extends MinibossThingEntity implements RangedAttackMob {

    private static final EntityDataAccessor<Boolean> MELEE;

    public BeastEntity(EntityType<? extends BeastEntity> entityType, Level world) {
        super(entityType, world);
        this.canGrief = true;
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(2, new BeastAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LivingEntity.class, 10.0F, 1.0, 1.2, (livingEntity) -> livingEntity.equals(this.getTarget())));
        this.goalSelector.addGoal(4, new ThingProjectileAttackGoal(this, 1.0, 20, 20, 16.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
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
    protected void defineSynchedData() {
        this.entityData.define(MELEE, false);
        super.defineSynchedData();
    }

    private void toggleMelee(){
        this.entityData.set(MELEE, !this.isMelee());
    }

    public boolean isMelee(){
        return this.entityData.get(MELEE);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.tickCount % 300 == 0)
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
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("IsMelee", this.isMelee());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("IsMelee") && nbt.getBoolean("IsMelee")){
            this.toggleMelee();
        }
        super.readAdditionalSaveData(nbt);
    }

    public static AttributeSupplier.Builder createBeastAttributes(){
        return createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 48);
    }

    static {
        MELEE = SynchedEntityData.defineId(BeastEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        for (int i = 0; i < 4; i++){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.level(), this);
            assimilationLiquid.setPosRaw(this.getX(), this.getY(0.5D), this.getZ());
            assimilationLiquid.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(assimilationLiquid.position()).normalize().scale(1.75D).add(new Vec3(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).scale(0.01)));
            this.level().addFreshEntity(assimilationLiquid);
        }
    }
}
