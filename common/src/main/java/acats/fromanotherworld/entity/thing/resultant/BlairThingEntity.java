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
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class BlairThingEntity extends MinibossThingEntity {

    public BlairThingEntity(EntityType<? extends BlairThingEntity> entityType, World world) {
        super(entityType, world);
    }

    private static final TrackedData<Integer> MOVE_COOLDOWN;
    private static final TrackedData<Integer> ATTACK;
    public static final int EMERGE_TIME_IN_TICKS = 40;
    public static final int RETREAT_TIME_IN_TICKS = 50;
    public static final int MOVE_COOLDOWN_IN_TICKS = 400;
    public static final int TIME_UNDERGROUND_IN_TICKS = 40;
    public static final int TIME_UNTIL_ATTACK_IN_TICKS = 60;

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new AbsorbGoal(this, STANDARD));
        this.goalSelector.add(1, new BlairThingSpecialAttacksGoal(this));
        this.goalSelector.add(2, new BlairThingAttackGoal(this, 1.0D, false));
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
    protected void mobTick() {
        this.setMoveCooldown(this.getMoveCooldown() + 1);
        if (this.getMoveCooldown() > MOVE_COOLDOWN_IN_TICKS)
            this.setMoveCooldown(0);
        if (this.getMoveCooldown() == MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS) && this.getTarget() != null && !this.isAiDisabled()){
            this.extinguish();
            this.rerollAttack();
            this.teleport2(this.getTarget().getPos().getX(), this.getTarget().getPos().getY(), this.getTarget().getPos().getZ());
        }
        super.mobTick();
    }

    private void teleport2(double x, double y, double z) {
        double g = y;
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        World world = this.getWorld();
        if (world.isChunkLoaded(blockPos)) {
            boolean bl2 = false;

            while(!bl2 && blockPos.getY() > world.getBottomY()) {
                BlockPos blockPos2 = blockPos.down();
                BlockState blockState = world.getBlockState(blockPos2);
                if (blockState.blocksMovement()) {
                    bl2 = true;
                } else {
                    --g;
                    blockPos = blockPos2;
                }
            }

            if (bl2) {
                this.requestTeleport(x, g, z);
                if (this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)){
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
    public float getMovementSpeed() {
        return this.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS) ? 0.0F : super.getMovementSpeed();
    }

    @Override
    protected void jump() {
    }

    @Override
    public boolean canThingFreeze() {
        return this.getMoveCooldown() > TIME_UNTIL_ATTACK_IN_TICKS && this.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getMoveCooldown() >= MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS))
            return false;
        return super.damage(source, amount);
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean cannotMerge() {
        return this.underground() || super.cannotMerge();
    }

    public void setMoveCooldown(int moveCooldown){
        this.dataTracker.set(MOVE_COOLDOWN, moveCooldown);
    }

    public int getMoveCooldown() {
        return this.dataTracker.get(MOVE_COOLDOWN);
    }

    public void setAttack(int attack){
        this.dataTracker.set(ATTACK, attack);
    }

    public int getAttack() {
        return this.dataTracker.get(ATTACK);
    }

    public static DefaultAttributeContainer.Builder createBlairThingAttributes(){
        return createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVE_COOLDOWN, 0);
        this.dataTracker.startTracking(ATTACK, 1);
    }

    @Override
    public void onDeath(DamageSource source) {
        DogBeastSpitterEntity dogSpitterEntity = EntityRegistry.DOGBEAST_SPITTER.get().create(this.getWorld());
        if (dogSpitterEntity != null) {
            dogSpitterEntity.setPosition(this.getPos());
            dogSpitterEntity.initializeFrom(this);
            dogSpitterEntity.canSpit = true;
            dogSpitterEntity.canGrief = true;
            this.getWorld().spawnEntity(dogSpitterEntity);
        }
        super.onDeath(source);
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
        MOVE_COOLDOWN = DataTracker.registerData(BlairThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        ATTACK = DataTracker.registerData(BlairThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
