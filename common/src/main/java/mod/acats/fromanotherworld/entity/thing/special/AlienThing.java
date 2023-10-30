package mod.acats.fromanotherworld.entity.thing.special;

import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.entity.goal.*;
import mod.acats.fromanotherworld.entity.interfaces.ImportantDeathMob;
import mod.acats.fromanotherworld.entity.interfaces.StalkerThing;
import mod.acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.spawning.SpawningManager;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import mod.acats.fromanotherworld.utilities.ProjectileUtilities;
import mod.acats.fromanotherworld.utilities.chunkloading.FAWChunkLoader;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AlienThing extends Thing implements StalkerThing, ImportantDeathMob {
    public AlienThing(EntityType<? extends AlienThing> entityType, Level world) {
        super(entityType, world);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.adaptiveKnockbackResist = 0.0F;
        this.timeSinceHitTarget = 0;
    }

    private static final EntityDataAccessor<Integer> FORM;
    private static final EntityDataAccessor<Integer> SWITCH_PROGRESS;
    private static final EntityDataAccessor<Integer> EMERGING;
    private static final EntityDataAccessor<Integer> BURROWING;

    public boolean fleeing;
    public boolean bored;
    private boolean leaping;
    private int timeSinceHitTarget;

    private static final double DEFAULT_MOVEMENT_SPEED = 0.43D;
    private static final double DEFAULT_ATTACK_DAMAGE = 7.0D;

    public static AttributeSupplier.Builder createAlienThingAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 24).add(Attributes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED).add(Attributes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE).add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FORM, 0);
        this.entityData.define(SWITCH_PROGRESS, 0);
        this.entityData.define(EMERGING, 0);
        this.entityData.define(BURROWING, 0);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(true);
        this.goalSelector.addGoal(0, new AlienThingSwimGoal(this, 0.5F));
        this.goalSelector.addGoal(1, new AlienThingFleeGoal(this));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new StalkGoal(this));
    }

    public int getIdealForm(@Nullable Player p) {
        if (p != null && p.distanceToSqr(this) < 1600) {
            return 0;
        }

        float f = 0.8F;
        if (this.getY() < ((float) this.level().getSeaLevel() * f + (float) this.level().getMinBuildHeight() * (1.0F - f)) * 0.5F) {
            return 1;
        }

        return 2;
    }

    private void setAttributes(double speed, double damage, double kbResist){
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(speed);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(damage);
        Objects.requireNonNull(this.getAttribute(Attributes.KNOCKBACK_RESISTANCE)).setBaseValue(kbResist);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.createChunkLoader();
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    private void setForm(int form){
        this.entityData.set(FORM, form);
    }
    public int getForm(){
        return this.entityData.get(FORM);
    }

    private void setSwitchProgress(int switchProgress){
        this.entityData.set(SWITCH_PROGRESS, switchProgress);
    }
    public int getSwitchProgress(){
        return this.entityData.get(SWITCH_PROGRESS);
    }
    public float getSwitchProgress2(){
        return 1.0F - (1.0F - this.getSwitchProgress() / 50.0F) * (1.0F - this.getSwitchProgress() / 50.0F);
    }

    private static final float ANIMATION_SPEED_MULTIPLIER = 2.0F;
    private boolean isEmerging(){
        return this.entityData.get(EMERGING) > 0;
    }
    public void tickEmerging(){
        int emerging = this.entityData.get(EMERGING);
        if (emerging > EMERGE_TIME)
            emerging = -1;
        this.entityData.set(EMERGING, emerging + 1);
    }
    private boolean isBurrowing(){
        return this.entityData.get(BURROWING) > 0;
    }
    private void tickBurrowing(){
        int burrowing = this.entityData.get(BURROWING);
        if (burrowing > EMERGE_TIME) {
            this.leave();
            return;
        }
        this.entityData.set(BURROWING, burrowing + 1);
    }

    private void leave(){
        if (!this.level().isClientSide()){
            SpawningManager spawningManager = SpawningManager.getSpawningManager((ServerLevel) this.level());
            spawningManager.alienThingsToSpawn++;
            spawningManager.setDirty();
            this.discard();
        }
    }

    private boolean burrowingOrEmerging(){
        return this.isBurrowing() || this.isEmerging();
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || this.burrowingOrEmerging();
    }

    @Override
    public int timeUntilBoredInThreeSeconds() {
        return 40;
    }

    private int clientForm;
    private int prevClientForm;
    @Override
    public void tick() {
        if (this.isEmerging())
            this.tickEmerging();
        else if (this.isBurrowing())
            this.tickBurrowing();

        if (this.level().isClientSide()) {
            this.prevClientForm = this.clientForm;
            this.clientForm = this.getForm();
        }

        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        if (!this.level().isClientSide()){
            this.adaptiveKnockbackResist *= 0.995F;
            if (!this.isThingFrozen()){
                if (this.leaping) {
                    this.grief(0, 1);
                    if (this.onGround()) {
                        this.leaping = false;
                    }
                }

                this.tickSpit();

                if (this.tickCount % 20 == 0){
                    if (this.getRandom().nextInt(5) == 0 && this.getForm() != this.getIdealForm(this.getTarget() instanceof Player p ? p : null)){
                        this.initiateSwitch();
                    }

                    if (this.fleeing || this.bored) {
                        this.escape();
                    }

                    if (this.tickCount % 60 == 0 &&
                            this.getTarget() != null &&
                            this.getForm() == 1 &&
                            this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) &&
                            this.getTarget().getY() > this.getY() + 1 &&
                            this.getNavigation().isDone()){
                        int x = Mth.floor(this.getX());
                        int y = Mth.floor(this.getY());
                        int z = Mth.floor(this.getZ());
                        if (this.breakOrPlaceable(new BlockPos(x, y, z)) && this.breakOrPlaceable(new BlockPos(x, y + 2, z))){
                            this.setPosRaw(x + 0.5D, y + 1, z + 0.5D);
                            this.level().destroyBlock(new BlockPos(x, y + 2, z), false, this);
                            this.level().setBlockAndUpdate(new BlockPos(x, y, z), Blocks.COBBLESTONE.defaultBlockState());
                        }
                    }

                    this.updateGiveUpChase();
                }
            }

            if (this.getSwitchProgress() > 0)
                this.tickSwitch();
        }
        super.customServerAiStep();
    }

    private void updateGiveUpChase() {
        if (this.getTarget() == null) {
            this.timeSinceHitTarget--;
        } else {
            this.timeSinceHitTarget++;
        }
        this.timeSinceHitTarget = Mth.clamp(this.timeSinceHitTarget, 0, 20);
        if (this.timeSinceHitTarget == 20) {
            this.bored();
        }
    }

    private double randomSpread() {
        return (this.getRandom().nextDouble() - 0.5D) * 3.0D;
    }

    private void tickSpit() {

        if (this.tickCount % 20 == 0 && this.getTarget() instanceof Player p && this.cheeseDetected(p)) {
            ProjectileUtilities.shootFromTo(new AssimilationLiquidEntity(this.level(), this), this, p, 2.0F, Vec3.ZERO);
        }

        BlockUtilities.forEachBlockInCubeCentredAt(this.blockPosition(), 1, blockPos -> {
            if (this.level().getFluidState(blockPos).is(FluidTags.LAVA) || this.level().getBlockState(blockPos).is(Blocks.FIRE)) {
                Vec3 target = new Vec3(blockPos.getX() + this.randomSpread(), blockPos.getY() + this.randomSpread(), blockPos.getZ() + this.randomSpread());
                ProjectileUtilities.shootFromTo(new AssimilationLiquidEntity(this.level(), this), this, target, 1.0F, Vec3.ZERO);
            }
        });
    }

    private boolean cheeseDetected(Player p) {
        double minHeight = this.getY();
        if (!this.isInWater()) {
            minHeight += 4;
        }
        return p.getY() > minHeight;
    }

    @Override
    public void threeSecondDelayServerTick() {
        super.threeSecondDelayServerTick();

        if (this.isThingFrozen()) {
            return;
        }

        if (this.tickCount % 300 == 0) {
            this.createChunkLoader();
        }

        if (this.getForm() == 1 &&
                this.getRandom().nextInt(20) == 0 &&
                this.breakOrPlaceable(this.blockPosition()) &&
                this.onGround()) {
            for (BlockState blockState:
                    this.level().getBlockStates(this.getBoundingBox().inflate(24)).toList()) {
                if (blockState.is(BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get())) {
                    return;
                }
            }
            this.level().setBlockAndUpdate(this.blockPosition(), BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().disguisedBlockState());
        }
    }

    private void createChunkLoader() {
        if (Config.WORLD_CONFIG.alienChunkLoading.get()) {
            FAWChunkLoader.create((ServerLevel) this.level(), (int) this.getX(), (int) this.getZ(), 5, 2);
        }
    }

    @Override
    public boolean canClimb() {
        return super.canClimb() && !this.fleeing;
    }

    @Override
    public boolean onClimbable() {
        return super.onClimbable() && !this.fleeing;
    }

    @Override
    public boolean canBurrow() {
        return super.canBurrow() && this.getSwitchProgress() == 0;
    }

    @Override
    public void bored() {
        this.escape();
        this.bored = true;
    }

    private boolean breakOrPlaceable(BlockPos pos){
        return EntityUtilities.canThingDestroy(pos, this.level()) || this.level().getBlockState(pos).isAir();
    }

    @Override
    public void aiStep() {
        if (this.getSwitchProgress() > 0){
            for (int i = 0; i < 20.0F * this.getSwitchProgress2(); i++){
                this.level().addParticle(ParticleRegistry.THING_GORE.get(), this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0, 0, 0);
            }
        }
        if (this.burrowingOrEmerging()){
            if (this.tickCount % 2 == 0) {
                this.level().playSound(null, this.blockPosition(), this.getBlockStateOn().getSoundType().getBreakSound(), SoundSource.HOSTILE, 2.0F, this.getVoicePitch());
            }
            this.digParticles();
        }
        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.burrowingOrEmerging()) {
            return false;
        }

        if (source.is(DamageTypes.LAVA)) {
            double speed = 1.0D;
            this.setDeltaMovement(
                    speed * (this.getRandom().nextFloat() - 0.5D) * 2.0D,
                    speed,
                    speed * (this.getRandom().nextFloat() - 0.5F) * 2.0D
            );
            this.leaping = true;
        }

        return super.hurt(source, amount);
    }

    private float adaptiveKnockbackResist;
    private static final float ADAPTATION_STRENGTH = 1.0F;
    @Override
    public void knockback(double d, double e, double f) {
        d *= 1.0 - Mth.clamp(this.adaptiveKnockbackResist, 0.0F, 1.0F);
        super.knockback(d, e, f);
        this.adaptiveKnockbackResist += (float) (d * ADAPTATION_STRENGTH);
    }

    private void initiateSwitch(){
        if (this.getSwitchProgress() == 0) {
            this.setSwitchProgress(1);
        }
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
        this.changeForm(this.getIdealForm(this.getTarget() instanceof Player p ? p : null));
    }

    public void changeForm(int form){
        this.setForm(form);

        switch (form) {
            case 0 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED, DEFAULT_ATTACK_DAMAGE, 0.0);
            case 1 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED * 0.75D, DEFAULT_ATTACK_DAMAGE * 1.25D, 1.0D);
            case 2 -> this.setAttributes(DEFAULT_MOVEMENT_SPEED, DEFAULT_ATTACK_DAMAGE * 0.5D, 1.0D);
        }

        this.canGrief = form == 0;
    }

    private Player stalkTarget;

    @Override
    public Player getStalkTarget() {
        return this.stalkTarget;
    }

    @Override
    public void setStalkTarget(Player stalkTarget) {
        this.stalkTarget = stalkTarget;
    }

    public void escape(){
        if (this.onGround() && !this.isThingFrozen() && !this.isBurrowing())
            this.tickBurrowing();
    }

    public String currentForm(){
        return switch (this.getForm()) {
            default -> "alien_thing_novella";
            case 1 -> "alien_thing_1951";
            case 2 -> "alien_thing_prequel";
        };
    }

    @Override
    protected float getSoundVolume() {
        return super.getSoundVolume() * 2.0F;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.clientForm != this.prevClientForm) {
            event.getController().forceAnimationReset();
        }
        if (this.burrowingOrEmerging() || this.isThingBurrowing() || this.isThingEmerging()){
            event.getController().setAnimationSpeed(ANIMATION_SPEED_MULTIPLIER);
            if (this.isBurrowing() || this.isThingBurrowing()){
                event.getController().setAnimation(FAWAnimations.BURROW);
            }
            else{
                event.getController().setAnimation(FAWAnimations.EMERGE);
            }
        }
        else{
            if (this.isThingFrozen()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay("misc.idle"));
                event.getController().setAnimationSpeed(0.0D);
            }
            else{
                event.getController().setAnimationSpeed(this.getForm() == 2 && event.isMoving() ? 2.0D : 1.0D);
                if (this.isUnderWater()){
                    event.getController().setAnimation(FAWAnimations.SWIM);
                }
                else{
                    if (event.isMoving() || (this.rotateWhenClimbing() && this.movingClimbing())){
                        event.getController().setAnimation(FAWAnimations.WALK);
                    }
                    else{
                        event.getController().setAnimation(RawAnimation.begin().thenPlay("misc.idle"));
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoEntity> PlayState novellaPredicate(AnimationState<E> event) {
        if (this.getForm() == 0 && !this.isThingFrozen()){
            event.getController().setAnimation(RawAnimation.begin().thenPlay("misc.head_tentacles"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState ttfawPredicate(AnimationState<E> event){
        if (this.getForm() == 1 && !this.isThingFrozen() && event.isMoving() && !this.isUnderWater()){
            if (this.isAggressive()){
                event.getController().setAnimation(RawAnimation.begin().thenPlay("move.arms_chase"));
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenPlay("move.arms_walk"));
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
    public void doEnchantDamageEffects(LivingEntity livingEntity, Entity entity) {
        if (!entity.isAlive() && entity instanceof Player) { // Hopefully this should prevent it from spawnkilling
            this.bored();
        }

        this.timeSinceHitTarget = 0;

        super.doEnchantDamageEffects(livingEntity, entity);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Fleeing", this.fleeing);
        nbt.putInt("Form", this.getForm());
        nbt.putInt("SwitchProgress", this.getSwitchProgress());
        nbt.putInt("Emerging", this.entityData.get(EMERGING));
        nbt.putInt("Burrowing", this.entityData.get(BURROWING));
        nbt.putBoolean("Bored", this.bored);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.fleeing = nbt.getBoolean("Fleeing");
        this.setForm(nbt.getInt("Form"));
        this.setSwitchProgress(nbt.getInt("SwitchProgress"));
        this.entityData.set(EMERGING, nbt.getInt("Emerging"));
        this.entityData.set(BURROWING, nbt.getInt("Burrowing"));
        this.bored = nbt.getBoolean("Bored");
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.importantDie(damageSource);
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.SPECIAL_MINIBOSS;
    }

    @Override
    public float getBurrowDepth() {
        return 0.0F;
    }

    @Override
    public BurrowType getBurrowType() {
        return Config.DIFFICULTY_CONFIG.burrowing.get() ? BurrowType.CAN_BURROW : BurrowType.CANNOT_BURROW;
    }

    static {
        FORM = SynchedEntityData.defineId(AlienThing.class, EntityDataSerializers.INT);
        SWITCH_PROGRESS = SynchedEntityData.defineId(AlienThing.class, EntityDataSerializers.INT);
        EMERGING = SynchedEntityData.defineId(AlienThing.class, EntityDataSerializers.INT);
        BURROWING = SynchedEntityData.defineId(AlienThing.class, EntityDataSerializers.INT);
    }
}
