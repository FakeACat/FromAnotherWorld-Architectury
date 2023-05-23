package acats.fromanotherworld.entity.thing.special;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.goal.AlienThingFleeGoal;
import acats.fromanotherworld.entity.goal.AlienThingSwimGoal;
import acats.fromanotherworld.entity.goal.StalkGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.interfaces.StalkerThing;
import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.spawning.SpawningManager;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Objects;

public class AlienThingEntity extends AbstractThingEntity implements StalkerThing {
    public AlienThingEntity(EntityType<? extends AlienThingEntity> entityType, World world) {
        super(entityType, world, false);
        this.experiencePoints = 25;
    }

    private static final TrackedData<Integer> FORM;
    private static final TrackedData<Integer> SWITCH_PROGRESS;
    private static final TrackedData<Integer> EMERGING;
    private static final TrackedData<Integer> BURROWING;

    public boolean fleeing;
    private int switchTimer;
    public boolean bored;

    private static final double DEFAULT_MOVEMENT_SPEED = 0.45D;
    private static final double DEFAULT_ATTACK_DAMAGE = 7.0D;
    private static final double DEFAULT_KNOCKBACK_RESISTANCE = 0.0D;

    public static DefaultAttributeContainer.Builder createAlienThingAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, DEFAULT_KNOCKBACK_RESISTANCE);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FORM, 0);
        this.dataTracker.startTracking(SWITCH_PROGRESS, 0);
        this.dataTracker.startTracking(EMERGING, 0);
        this.dataTracker.startTracking(BURROWING, 0);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(true);
        this.goalSelector.add(0, new AlienThingSwimGoal(this, 0.5F));
        this.goalSelector.add(1, new AlienThingFleeGoal(this));
        this.goalSelector.add(2, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(3, new StalkGoal(this));
    }


    private void setAttributes(double speed, double damage, double kbResist){
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(speed);
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).setBaseValue(damage);
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(kbResist);
    }
    private void setForm(int form){
        this.dataTracker.set(FORM, form);
    }
    public int getForm(){
        return this.dataTracker.get(FORM);
    }

    private void setSwitchProgress(int switchProgress){
        this.dataTracker.set(SWITCH_PROGRESS, switchProgress);
    }
    public int getSwitchProgress(){
        return this.dataTracker.get(SWITCH_PROGRESS);
    }
    public float getSwitchProgress2(){
        return 1.0F - (1.0F - this.getSwitchProgress() / 50.0F) * (1.0F - this.getSwitchProgress() / 50.0F);
    }

    private static final int EMERGE_TIME_TICKS = 50;
    private static final float ANIMATION_SPEED_MULTIPLIER = 2.0F;
    private boolean isEmerging(){
        return this.dataTracker.get(EMERGING) > 0;
    }
    public void tickEmerging(){
        int emerging = this.dataTracker.get(EMERGING);
        if (emerging > EMERGE_TIME_TICKS)
            emerging = -1;
        this.dataTracker.set(EMERGING, emerging + 1);
    }
    private boolean isBurrowing(){
        return this.dataTracker.get(BURROWING) > 0;
    }
    private void tickBurrowing(){
        int burrowing = this.dataTracker.get(BURROWING);
        if (burrowing > EMERGE_TIME_TICKS) {
            this.leave();
            return;
        }
        this.dataTracker.set(BURROWING, burrowing + 1);
    }

    private void leave(){
        if (!this.getWorld().isClient()){
            SpawningManager spawningManager = SpawningManager.getSpawningManager((ServerWorld) this.getWorld());
            spawningManager.alienThingsToSpawn++;
            spawningManager.markDirty();
            this.discard();
        }
    }

    private boolean burrowingOrEmerging(){
        return this.isBurrowing() || this.isEmerging();
    }

    @Override
    public boolean isAiDisabled() {
        return super.isAiDisabled() || this.burrowingOrEmerging();
    }

    @Override
    public int timeUntilBored() {
        return 20;
    }

    @Override
    public void tick() {
        if (this.isEmerging())
            this.tickEmerging();
        else if (this.isBurrowing())
            this.tickBurrowing();

        super.tick();
    }

    @Override
    protected void mobTick() {
        if (!this.world.isClient()){
            if (!this.isThingFrozen()){
                PlayerEntity player = this.getWorld().getClosestPlayer(this, -1.0F);
                if (player == null || player.squaredDistanceTo(this) > 16384){
                    this.leave();
                    return;
                }
                switchTimer++;
                if (switchTimer > 1800){
                    this.switchTimer = 0;
                    if (this.getRandom().nextBoolean())
                        this.initiateSwitch();
                }
                if (this.age % 20 == 0){
                    if (this.fleeing || this.bored)
                        this.escape();
                    if (this.age % 60 == 0 &&
                            this.getTarget() != null &&
                            this.getForm() == 1 &&
                            this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) &&
                            this.getTarget().getY() > this.getY() + 1 &&
                            this.getNavigation().isIdle()){
                        int x = MathHelper.floor(this.getX());
                        int y = MathHelper.floor(this.getY());
                        int z = MathHelper.floor(this.getZ());
                        if (this.breakOrPlaceable(new BlockPos(x, y, z)) && this.breakOrPlaceable(new BlockPos(x, y + 2, z))){
                            this.setPos(x + 0.5D, y + 1, z + 0.5D);
                            this.world.breakBlock(new BlockPos(x, y + 2, z), false, this);
                            this.world.setBlockState(new BlockPos(x, y, z), Blocks.COBBLESTONE.getDefaultState());
                        }
                    }
                }
            }

            if (this.getSwitchProgress() > 0)
                this.tickSwitch();
        }
        super.mobTick();
    }

    @Override
    public boolean canClimb() {
        return !this.fleeing;
    }

    @Override
    public boolean isClimbing() {
        return super.isClimbing() && !this.fleeing;
    }

    @Override
    public void bored() {
        this.escape();
        this.bored = true;
    }

    private boolean breakOrPlaceable(BlockPos pos){
        return FromAnotherWorld.canThingDestroy(world.getBlockState(pos)) || world.getBlockState(pos).isAir();
    }

    @Override
    public void tickMovement() {
        if (this.getSwitchProgress() > 0){
            for (int i = 0; i < 20.0F * this.getSwitchProgress2(); i++){
                this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);
            }
        }
        if (this.burrowingOrEmerging()){
            Random random = this.getRandom();
            BlockState blockState = this.getSteppingBlockState();
            if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                for(int i = 0; i < 30; ++i) {
                    double d = this.getX() + (double)MathHelper.nextBetween(random, -0.7F, 0.7F);
                    double e = this.getY();
                    double f = this.getZ() + (double)MathHelper.nextBetween(random, -0.7F, 0.7F);
                    this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        }
        super.tickMovement();
    }

    private void initiateSwitch(){
        this.setSwitchProgress(1);
    }

    private void tickSwitch(){
        int progress = this.getSwitchProgress();
        if (progress == 50)
            this.changeForm();
        if (progress > 100){
            this.setSwitchProgress(0);
        }
        else{
            this.setSwitchProgress(progress + 1);
        }
    }

    private void changeForm(){
        this.heal(this.getMaxHealth());
        int form = (this.getForm() + 1) % 3;
        changeForm(form);
    }

    public void changeForm(int form){
        this.setForm(form);

        switch (form) {
            case 0 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED, DEFAULT_ATTACK_DAMAGE, DEFAULT_KNOCKBACK_RESISTANCE);
            case 1 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED * 0.75D, DEFAULT_ATTACK_DAMAGE * 1.25D, 1.0D);
            case 2 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED * 1.125D, DEFAULT_ATTACK_DAMAGE * 0.75D, 0.5D);
        }

        this.canHunt = this.getRandom().nextInt(5) == 0;
        this.canGrief = this.getRandom().nextInt(5) == 0;
        this.canShootNeedles = form == 2 && this.getRandom().nextBoolean();
    }

    private PlayerEntity stalkTarget = null;
    @Override
    public PlayerEntity getStalkTarget(){
        float d2 = AbstractThingEntity.HUNTING_RANGE * AbstractThingEntity.HUNTING_RANGE;

        if (this.stalkTarget != null && !this.stalkTarget.isCreative() && !this.stalkTarget.isSpectator() && this.stalkTarget.squaredDistanceTo(this) < d2)
            return this.stalkTarget;

        this.stalkTarget = this.world.getClosestPlayer(this, AbstractThingEntity.HUNTING_RANGE);

        if (this.stalkTarget != null && !this.stalkTarget.isCreative() && !this.stalkTarget.isSpectator() && this.stalkTarget.squaredDistanceTo(this) < d2)
            return this.stalkTarget;

        return null;
    }

    public void escape(){
        if (this.isOnGround() && !this.isThingFrozen() && !this.isBurrowing())
            this.tickBurrowing();
    }

    public String currentForm(){
        return switch (this.getForm()) {
            default -> "alien_thing_novella";
            case 1 -> "alien_thing_1951";
            case 2 -> "alien_thing_prequel";
        };
    }

    private String currentAnimation(){
        return "animation." + this.currentForm();
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.burrowingOrEmerging()){
            event.getController().setAnimationSpeed(ANIMATION_SPEED_MULTIPLIER);
            if (this.isBurrowing()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".burrow"));
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".emerge"));
            }
        }
        else{
            if (this.isThingFrozen()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".idle"));
                event.getController().setAnimationSpeed(0.0D);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimationSpeed(this.getForm() == 2 && event.isMoving() ? 2.0D : 1.0D);
            if (this.isSubmergedInWater()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".swim"));
            }
            else{
                if (event.isMoving() || (this.rotateWhenClimbing() && this.movingClimbing())){
                    event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".walk"));
                }
                else{
                    event.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentAnimation() + ".idle"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoEntity> PlayState novellaPredicate(AnimationState<E> event) {
        if (this.getForm() == 0 && !this.isThingFrozen()){
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.alien_thing_novella.head_tentacles"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState ttfawPredicate(AnimationState<E> event){
        if (this.getForm() == 1 && !this.isThingFrozen() && event.isMoving() && !this.isSubmergedInWater()){
            if (this.isAttacking()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.alien_thing_1951.arms_chase"));
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.alien_thing_1951.arms_walk"));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "controller2", 0, this::novellaPredicate));
        controllerRegistrar.add(new AnimationController<>(this, "controller3", 0, this::ttfawPredicate));
    }

    @Override
    public boolean rotateWhenClimbing() {
        return this.getForm() == 2 && !this.fleeing;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Fleeing", this.fleeing);
        nbt.putInt("SwitchTimer", this.switchTimer);
        nbt.putInt("Form", this.getForm());
        nbt.putInt("SwitchProgress", this.getSwitchProgress());
        nbt.putInt("Emerging", this.dataTracker.get(EMERGING));
        nbt.putInt("Burrowing", this.dataTracker.get(BURROWING));
        nbt.putBoolean("Bored", this.bored);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.fleeing = nbt.getBoolean("Fleeing");
        this.switchTimer = nbt.getInt("SwitchTimer");
        this.setForm(nbt.getInt("Form"));
        this.setSwitchProgress(nbt.getInt("SwitchProgress"));
        this.dataTracker.set(EMERGING, nbt.getInt("Emerging"));
        this.dataTracker.set(BURROWING, nbt.getInt("Burrowing"));
        this.bored = nbt.getBoolean("Bored");
    }

    @Override
    public Strength getFormStrength() {
        return Strength.MINIBOSS;
    }

    static {
        FORM = DataTracker.registerData(AlienThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        SWITCH_PROGRESS = DataTracker.registerData(AlienThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        EMERGING = DataTracker.registerData(AlienThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        BURROWING = DataTracker.registerData(AlienThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
