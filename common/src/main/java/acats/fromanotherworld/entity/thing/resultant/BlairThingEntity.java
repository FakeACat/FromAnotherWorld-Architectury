package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.BlairThingAttackGoal;
import acats.fromanotherworld.entity.goal.BlairThingSpecialAttacksGoal;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlairThingEntity extends MinibossThingEntity {

    public BlairThingEntity(EntityType<? extends BlairThingEntity> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Integer> MOVE_COOLDOWN;
    private static final EntityDataAccessor<Integer> ATTACK;
    public static final int EMERGE_TIME_IN_TICKS = 40;
    public static final int RETREAT_TIME_IN_TICKS = 50;
    public static final int MOVE_COOLDOWN_IN_TICKS = 400;
    public static final int TIME_UNDERGROUND_IN_TICKS = 40;
    public static final int TIME_UNTIL_ATTACK_IN_TICKS = 60;

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(1, new BlairThingSpecialAttacksGoal(this));
        this.goalSelector.addGoal(2, new BlairThingAttackGoal(this, 1.0D, false));
    }

    @Override
    public boolean canClimb() {
        return false;
    }

    @Override
    double getStartingHealth() {
        return 150.0D;
    }

    @Override
    double getScalingHealth() {
        return 75.0D;
    }

    @Override
    double getStartingSpeed() {
        return 0.1D;
    }

    @Override
    double getScalingSpeed() {
        return 0.0D;
    }

    @Override
    double getStartingDamage() {
        return 12.0D;
    }

    @Override
    double getScalingDamage() {
        return 6.0D;
    }

    public void rerollAttack(){
        this.setAttack(this.getRandom().nextInt(3));
    }

    @Override
    protected void customServerAiStep() {
        this.setMoveCooldown(this.getMoveCooldown() + 1);
        if (this.getMoveCooldown() > MOVE_COOLDOWN_IN_TICKS)
            this.setMoveCooldown(0);
        if (this.getMoveCooldown() == MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS) && this.getTarget() != null && !this.isNoAi()){
            this.clearFire();
            this.rerollAttack();
            this.teleport2(this.getTarget().position().x(), this.getTarget().position().y(), this.getTarget().position().z());
        }
        super.customServerAiStep();
    }

    private void teleport2(double x, double y, double z) {
        double g = y;
        BlockPos blockPos = BlockPos.containing(x, y, z);
        Level world = this.level();
        if (world.hasChunkAt(blockPos)) {
            boolean bl2 = false;

            while(!bl2 && blockPos.getY() > world.getMinBuildHeight()) {
                BlockPos blockPos2 = blockPos.below();
                BlockState blockState = world.getBlockState(blockPos2);
                if (blockState.blocksMotion()) {
                    bl2 = true;
                } else {
                    --g;
                    blockPos = blockPos2;
                }
            }

            if (bl2) {
                this.teleportTo(x, g, z);
                if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    this.grief(0, 1);
                }
            }
        }

        this.getNavigation().stop();
    }

    private boolean underground(){
        return this.getMoveCooldown() > MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS);
    }

    @Override
    public boolean isInvisible() {
        return super.isInvisible() || this.getMoveCooldown() >= MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS) && this.getMoveCooldown() <= MOVE_COOLDOWN_IN_TICKS - EMERGE_TIME_IN_TICKS;
    }

    @Override
    public float getSpeed() {
        return this.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS) ? 0.0F : super.getSpeed();
    }

    @Override
    protected void jumpFromGround() {
    }

    @Override
    public boolean canThingFreeze() {
        return this.getMoveCooldown() > TIME_UNTIL_ATTACK_IN_TICKS && this.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getMoveCooldown() >= MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean cannotMerge() {
        return this.underground() || super.cannotMerge();
    }

    public void setMoveCooldown(int moveCooldown){
        this.entityData.set(MOVE_COOLDOWN, moveCooldown);
    }

    public int getMoveCooldown() {
        return this.entityData.get(MOVE_COOLDOWN);
    }

    public void setAttack(int attack){
        this.entityData.set(ATTACK, attack);
    }

    public int getAttack() {
        return this.entityData.get(ATTACK);
    }

    public static AttributeSupplier.Builder createBlairThingAttributes(){
        return createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 48).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOVE_COOLDOWN, 0);
        this.entityData.define(ATTACK, 1);
    }

    @Override
    public void die(DamageSource source) {
        DogBeastSpitterEntity dogSpitterEntity = EntityRegistry.DOGBEAST_SPITTER.get().create(this.level());
        if (dogSpitterEntity != null) {
            dogSpitterEntity.setPos(this.position());
            dogSpitterEntity.initializeFrom(this);
            dogSpitterEntity.canSpit = true;
            dogSpitterEntity.canGrief = true;
            this.level().addFreshEntity(dogSpitterEntity);
        }
        super.die(source);
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (this.getMoveCooldown() > MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + RETREAT_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS)){
            if (this.getMoveCooldown() > MOVE_COOLDOWN_IN_TICKS - EMERGE_TIME_IN_TICKS){
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.blair_thing.emerge"));
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.blair_thing.retreat"));
            }
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.blair_thing.idle"));
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoEntity> PlayState predicate2(AnimationState<E> event) {
        if (this.getAttack() == 0 && !this.isThingFrozen()){
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.blair_thing.spit"));
        }
        else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "attackController", 0, this::predicate2));
    }

    static {
        MOVE_COOLDOWN = SynchedEntityData.defineId(BlairThingEntity.class, EntityDataSerializers.INT);
        ATTACK = SynchedEntityData.defineId(BlairThingEntity.class, EntityDataSerializers.INT);
    }
}
