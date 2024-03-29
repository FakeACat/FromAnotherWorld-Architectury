package mod.acats.fromanotherworld.entity.thing.resultant;

import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.entity.goal.AbsorbGoal;
import mod.acats.fromanotherworld.entity.goal.DirectedWanderGoal;
import mod.acats.fromanotherworld.entity.goal.ThingAttackGoal;
import mod.acats.fromanotherworld.entity.goal.ThingProjectileAttackGoal;
import mod.acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.acats.fromanotherworld.utilities.ProjectileUtilities;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Beast extends MinibossThing implements RangedAttackMob {

    private static final EntityDataAccessor<Boolean> MELEE;
    private static final int MELEE_ACTIVATION_DIST = 12;
    private static final int RANGED_ACTIVATION_DIST = 16;

    private Goal meleeGoal;
    private Goal rangedGoal;

    public Beast(EntityType<? extends Beast> entityType, Level world) {
        super(entityType, world);
        this.canGrief = true;
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD, Config.DIFFICULTY_CONFIG.beastMergeChance.get()));
        this.meleeGoal = new ThingAttackGoal(this, 1.0D, false);
        this.rangedGoal = new ThingProjectileAttackGoal(this, 1.0, 10, 10, 48.0F);
        this.goalSelector.addGoal(2, rangedGoal);
        this.goalSelector.addGoal(3, new DirectedWanderGoal(this, 1.0D));
    }

    private void updateGoals(){
        if (this.meleeGoal != null && this.rangedGoal != null){
            if (this.isMelee()){
                this.goalSelector.addGoal(2, this.meleeGoal);
                this.goalSelector.removeGoal(this.rangedGoal);
            }
            else{
                this.goalSelector.addGoal(2, this.rangedGoal);
                this.goalSelector.removeGoal(this.meleeGoal);
            }
        }
    }

    @Override
    public boolean canClimb() {
        return false;
    }

    @Override
    public float maxUpStep() {
        return 2.0F;
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
        this.updateGoals();
    }

    public boolean isMelee(){
        return this.entityData.get(MELEE);
    }

    @Override
    public void threeSecondDelayServerTick() {
        super.threeSecondDelayServerTick();

        if (this.getTarget() != null){
            double dist = this.getTarget().distanceToSqr(this);
            if ((dist > RANGED_ACTIVATION_DIST * RANGED_ACTIVATION_DIST && this.isMelee()) || (dist < MELEE_ACTIVATION_DIST * MELEE_ACTIVATION_DIST && !this.isMelee())){
                this.toggleMelee();
            }
        }
    }

    private <E extends GeoEntity> PlayState predicateTentacles(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (this.isMelee()) {
            event.getController().setAnimation(FAWAnimations.OPEN_MOUTH_THEN_MELEE);
        }
        else{
            event.getController().setAnimation(FAWAnimations.CLOSE_MOUTH);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(FAWAnimations.alwaysPlaying(this));
        controllerRegistrar.add(FAWAnimations.defaultThingNoChase(this));
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
        MELEE = SynchedEntityData.defineId(Beast.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        ProjectileUtilities.shootFromTo(new AssimilationLiquidEntity(this.level(), this), this, target, 3.0F, new Vec3(1.25D, 0.5D, 0.0D).multiply(this.scaleFactor(), this.scaleFactor(), this.scaleFactor()));
    }
}
