package mod.acats.fromanotherworld.entity.thing;

import mod.acats.fromanotherlibrary.config.v2.properties.FloatProperty;
import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.CorpseBlock;
import mod.acats.fromanotherworld.block.entity.TunnelBlockEntity;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.constants.VariantID;
import mod.acats.fromanotherworld.entity.goal.ThingTargetGoal;
import mod.acats.fromanotherworld.entity.interfaces.CoordinatedThing;
import mod.acats.fromanotherworld.entity.interfaces.MaybeThing;
import mod.acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import mod.acats.fromanotherworld.entity.navigation.ThingNavigation;
import mod.acats.fromanotherworld.entity.projectile.NeedleEntity;
import mod.acats.fromanotherworld.memory.Aggression;
import mod.acats.fromanotherworld.memory.ThingBaseOfOperations;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.registry.SoundRegistry;
import mod.acats.fromanotherworld.tags.BlockTags;
import mod.acats.fromanotherworld.tags.DamageTypeTags;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import mod.acats.fromanotherworld.utilities.ServerUtilities;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class Thing extends Monster implements GeoEntity, MaybeThing, CoordinatedThing {
    private static final EntityDataAccessor<Byte> VARIANT_ID;
    private static final EntityDataAccessor<Boolean> HIBERNATING;
    private static final EntityDataAccessor<Float> COLD;
    private static final EntityDataAccessor<Boolean> CLIMBING;
    private static final EntityDataAccessor<Integer> BURROW_PROGRESS;
    private static final EntityDataAccessor<Byte> DISGUISE_PROGRESS;
    public static final int BURROW_TIME = 50;
    public static final int UNDERGROUND_TIME = 60;
    public static final int EMERGE_TIME = 50;
    private static final int BURROW_COOLDOWN = 600;
    protected Thing(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.xpReward = this.getThingCategory().getXpReward();
        if (!this.level().isClientSide()){
            if (!entityType.is(EntityTags.THINGS)){
                FromAnotherWorld.LOGGER.error(this.getEncodeId() + " extends Thing but is not in the things tag!");
            }

            if (this.getThingCategory().canHaveSpecialAbilities()){
                this.setRareAbilities(Config.DIFFICULTY_CONFIG.specialBehaviourRarity.get());
            }
        }
    }

    public static boolean hostileTowardsPossibleThreat(@Nullable LivingEntity target, boolean currentThreat) {
        if (target == null || !target.canBeSeenAsEnemy()) {
            return false;
        }
        return !target.getType().is(EntityTags.THING_ALLIES) &&
                !EntityUtilities.isThing(target) &&
                (currentThreat ||
                        EntityUtilities.canAssimilate(target) ||
                        target.getType().is(EntityTags.ATTACKABLE_BUT_NOT_ASSIMILABLE));
    }
    public static boolean hostileTowards(@Nullable Entity target) {
        if (!(target instanceof LivingEntity e)) {
            return false;
        }
        return Thing.hostileTowardsPossibleThreat(e, false);
    }

    public void initializeFrom(Entity parent){
        if (parent.level() instanceof ServerLevel serverWorld){
            this.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(parent.blockPosition()), MobSpawnType.CONVERSION, null, null);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.faw$updateBase();
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level world) {
        return new ThingNavigation(this, world);
    }

    @Override
    public float maxUpStep() {
        return 1.5F;
    }

    public boolean canSpit;
    public boolean canHunt;
    public boolean canGrief;
    public boolean canShootNeedles;
    public static final int HUNTING_RANGE = 128;
    public LivingEntity currentThreat;
    protected int timeSinceLastSeenTarget = 0;
    private int alertSoundCooldown = 0;
    private boolean stopClimbing = false;
    private CompoundTag victim = null;


    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public byte getVariantID(){
        return this.entityData.get(VARIANT_ID);
    }
    public void setVariantID(byte variantID){
        this.entityData.set(VARIANT_ID, variantID);
    }

    public boolean hibernating(){
        return this.entityData.get(HIBERNATING);
    }
    public void setHibernating(boolean asleep){
        this.entityData.set(HIBERNATING, asleep);
    }

    public float getCold(){
        return this.entityData.get(COLD);
    }
    public void setCold(float cold){
        this.entityData.set(COLD, Mth.clamp(cold, 0.0F, 1.0F));
    }

    public boolean isClimbingWall(){
        return this.entityData.get(CLIMBING) && this.canClimb();
    }
    public void setClimbingWall(boolean climbingWall){
        this.entityData.set(CLIMBING, climbingWall);
    }

    public int getBurrowProgress() {
        return this.entityData.get(BURROW_PROGRESS);
    }
    public void setBurrowProgress(int burrowProgress) {
        this.entityData.set(BURROW_PROGRESS, burrowProgress);
    }
    public boolean isThingBurrowing() {
        return this.getBurrowProgress() != 0 && this.getBurrowProgress() < BURROW_TIME;
    }
    public boolean isThingUnderground() {
        return this.getBurrowProgress() >= BURROW_TIME && this.getBurrowProgress() <= BURROW_TIME + UNDERGROUND_TIME;
    }
    public boolean isThingEmerging() {
        return this.getBurrowProgress() > BURROW_TIME + UNDERGROUND_TIME;
    }
    private int burrowCooldown = 0;

    public void setVictim(CompoundTag tag) {
        this.victim = tag;
        this.victim.putUUID("UUID", UUID.randomUUID());
    }

    public byte getDisguiseProgress() {
        return this.entityData.get(DISGUISE_PROGRESS);
    }
    public void setDisguiseProgress(byte disguiseProgress) {
        this.entityData.set(DISGUISE_PROGRESS, disguiseProgress);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, VariantID.DEFAULT);
        this.entityData.define(HIBERNATING, false);
        this.entityData.define(COLD, 0.0F);
        this.entityData.define(CLIMBING, false);
        this.entityData.define(BURROW_PROGRESS, 0);
        this.entityData.define(DISGUISE_PROGRESS, (byte) 0);
    }

    @Override
    protected @NotNull ResourceLocation getDefaultLootTable() {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "entities/thing/the_thing_default");
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.shouldMergeOnAssimilate() && ((PossibleDisguisedThing) target).faw$getSupercellConcentration() > 0) {
            return false;
        }
        return Thing.hostileTowardsPossibleThreat(target, target == this.currentThreat) && super.canAttack(target);
    }

    public void addThingTargets(boolean prioritisePlayer){
        this.targetSelector.addGoal(prioritisePlayer ? 0 : 1, new ThingTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public int timeUntilBoredInThreeSeconds(){
        return 100;
    }

    public boolean canClimb(){
        return true;
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbingWall() || super.onClimbable();
    }
    public boolean rotateWhenClimbing(){
        return false;
    }
    public float offsetWhenClimbing(){
        return 0.0F;
    }
    public float climbRotateProgress = 0.0F;
    public float nextClimbRotateProgress = 0.0F;
    public boolean movingClimbing(){
        return this.onClimbable();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (this.getThingCategory()){
            case FODDER, SPLIT -> SoundRegistry.WEAK_HURT.get();
            case MINIBOSS, SPECIAL_MINIBOSS -> SoundRegistry.STRONG_HURT.get();
            default -> SoundRegistry.GENERAL_HURT.get();
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return switch (this.getThingCategory()){
            case FODDER, SPLIT -> SoundRegistry.WEAK_DEATH.get();
            case MINIBOSS, SPECIAL_MINIBOSS -> SoundRegistry.STRONG_DEATH.get();
            default -> SoundRegistry.GENERAL_DEATH.get();
        };
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return switch (this.getThingCategory()){
            case FODDER, SPLIT -> SoundRegistry.WEAK_AMBIENT.get();
            case MINIBOSS, SPECIAL_MINIBOSS -> SoundRegistry.STRONG_AMBIENT.get();
            default -> SoundRegistry.GENERAL_AMBIENT.get();
        };
    }

    protected SoundEvent getAlertSound(){
        return switch (this.getThingCategory()){
            case FODDER, SPLIT -> SoundRegistry.WEAK_ALERT.get();
            case MINIBOSS, SPECIAL_MINIBOSS -> SoundRegistry.STRONG_ALERT.get();
            default -> null;
        };
    }

    @Override
    public float getVoicePitch() {
        return switch (this.getThingCategory()){
            default -> super.getVoicePitch();
            case ELITE -> super.getVoicePitch() * 0.8F;
            case MERGED -> super.getVoicePitch() * 0.6F;
        };
    }

    public int prevClientBurrowProgress = 0;
    public int clientBurrowProgress = 0;

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.getDisguiseProgress() > 0) {
                this.setDisguiseProgress((byte) (this.getDisguiseProgress() + 1));
                if (this.getDisguiseProgress() == 60) {
                    this.disguise();
                }
            }
            if (this.burrowCooldown > 0) {
                burrowCooldown--;
            }
            if (this.getBurrowProgress() > 0){
                this.setBurrowProgress(this.getBurrowProgress() + 1);
                if (this.getBurrowProgress() == BURROW_TIME + UNDERGROUND_TIME / 2 &&
                        this.randomTeleport(this.burrowX + 0.5D, this.burrowY, this.burrowZ + 0.5D, false) &&
                        this.getBurrowType() == BurrowType.REQUIRES_TUNNEL) {
                    this.lastTunnelExit = new BlockPos(this.burrowX, this.burrowY, this.burrowZ);
                }
                if (this.getBurrowProgress() == BURROW_TIME + UNDERGROUND_TIME + EMERGE_TIME) {
                    this.setBurrowProgress(0);
                }
            }
            if (this.canClimb()){
                this.tickClimb();
            }
            if (this.tickCount % 10 == 0){
                this.halfSecondDelayServerTick();
                if (this.tickCount % 60 == 0){
                    this.threeSecondDelayServerTick();
                }
            }
        }
        else {
            if (this.getDisguiseProgress() > 0) {
                for (int i = 0; i < this.getDisguiseProgress() / 3; i++) {
                    this.level().addParticle(ParticleRegistry.THING_GORE.get(), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0, 0, 0);
                }
            }
            if (this.isThingBurrowing() || this.isThingEmerging()) {
                this.digParticles();
                this.prevClientBurrowProgress = this.clientBurrowProgress;
                this.clientBurrowProgress = this.getBurrowProgress();
            }
            if (this.rotateWhenClimbing()){
                this.tickClimbRotation();
            }
        }
        if (this.getAlertSound() != null){
            if (alertSoundCooldown > 0){
                alertSoundCooldown--;
            }
            else if (this.isAggressive()){
                alertSoundCooldown = 6000;
                this.playSound(this.getAlertSound(), this.getSoundVolume(), this.getVoicePitch());
            }
        }
    }

    private void tickClimb() {
        if (!this.stopClimbing) {
            this.setClimbingWall(this.horizontalCollision);
            if (this.verticalCollision && !this.verticalCollisionBelow) {
                if (this.canGrief) {
                    this.grief(1, 3);
                }
                else {
                    this.stopClimbing = true;
                }
            }
        }
        else{
            this.setClimbingWall(false);
        }
        if (this.onGround() && this.getRandom().nextInt(60) == 0) {
            this.stopClimbing = false;
        }
    }

    private void tickClimbRotation() {
        if (this.onClimbable()) {
            this.climbRotateProgress = this.nextClimbRotateProgress;
            this.nextClimbRotateProgress = Math.min(this.climbRotateProgress + 0.05F, 1.0F);
        }
        else {
            this.climbRotateProgress = this.nextClimbRotateProgress;
            this.nextClimbRotateProgress = Math.max(this.climbRotateProgress - 0.1F, 0.0F);
        }
    }

    public void halfSecondDelayServerTick(){
        if (!EntityUtilities.isVulnerable(this)){
            this.heal(1.0F);
        }

        boolean bl = this.getTarget() != null;

        if (bl && this.canGrief && !this.isNoAi()) {
            this.grief(this.getTarget().getY() < this.getY() - 3 ? -1 : 0, 4);
        }

        if (this.hibernating()){
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
            if (bl) {
                this.setHibernating(false);
            }
        }
    }

    public void threeSecondDelayServerTick(){

        this.faw$updateBase();

        if (this.canThingFreeze())
            this.tickFreeze();

        if (this.getTarget() == null){
            this.timeSinceLastSeenTarget++;
            if (timeSinceLastSeenTarget > this.timeUntilBoredInThreeSeconds()){
                this.bored();
                this.timeSinceLastSeenTarget = 0;
            }
        }
        else{
            this.timeSinceLastSeenTarget = 0;
            if (this.canShootNeedles && !this.isNoAi() && this.tickCount % 300 == 0){
                for (int i = 0; i < 50; i++){
                    NeedleEntity needleEntity = new NeedleEntity(this.level(), this.getX(), this.getRandomY(), this.getZ(), this);
                    needleEntity.setDeltaMovement(new Vec3((random.nextDouble() - 0.5D) * 5, random.nextDouble() / 2, (random.nextDouble() - 0.5D) * 5));
                    this.level().addFreshEntity(needleEntity);
                }
            }
        }

        if (this.canShootNeedles && !this.isNoAi() && this.tickCount % 300 == 240){
            this.playSound(SoundRegistry.STRONG_AMBIENT.get(), 1.0F, 0.4F);
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 6, false, false));
        }
    }

    public boolean canDisguise() {
        return this.faw$getAggression() == Aggression.HIDING &&
                this.victim != null &&
                !this.isThingFrozen() &&
                !this.isThingEmerging() &&
                !this.isThingUnderground() &&
                !this.isThingBurrowing();
    }

    private void disguise() {
        if (!this.level().isClientSide()) {
            Optional<Entity> optionalEntity = EntityType.create(this.victim, this.level());
            @Nullable LivingEntity victimEntity = (LivingEntity) optionalEntity.orElse(null);
            if (victimEntity != null) {
                victimEntity.setHealth(victimEntity.getMaxHealth());
                victimEntity.setRemainingFireTicks(this.getRemainingFireTicks());
                victimEntity.removeAllEffects();
                victimEntity.setPos(this.position());
                victimEntity.setXRot(this.getXRot());
                victimEntity.setYBodyRot(this.yBodyRot);
                victimEntity.setYHeadRot(this.yHeadRot);
                if (this.level().addFreshEntity(victimEntity)) {
                    this.discard();
                }
                else {
                    this.setDisguiseProgress((byte) 0);
                }
            }
        }
    }

    private @Nullable ThingBaseOfOperations base = null;

    @Override
    public Optional<ThingBaseOfOperations> faw$getBase() {
        return Optional.ofNullable(this.base);
    }

    @Override
    public void faw$setBase(@Nullable ThingBaseOfOperations base) {
        this.base = base;
    }

    public void grief(int yOffset, int chanceDenominator) {
        BlockUtilities.grief(this.level(), Mth.floor(1 + this.getDimensions(this.getPose()).width / 2), Mth.floor(this.getX()), Mth.floor(this.getY()) + yOffset, Mth.floor(this.getZ()), this, chanceDenominator);
    }

    public void digParticles() {
        RandomSource random = this.getRandom();
        BlockState blockState = this.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for(int i = 0; i < 30; ++i) {
                double d = this.getX() + (double)Mth.randomBetween(random, -0.7F, 0.7F);
                double e = this.getY();
                double f = this.getZ() + (double)Mth.randomBetween(random, -0.7F, 0.7F);
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    public void bored(){
        if (this.canDisguise()) {
            this.setDisguiseProgress((byte) 1);
        }
    }

    private int burrowX = 0;
    private int burrowY = 0;
    private int burrowZ = 0;

    public void burrowTo(int x, int y, int z) {
        if (this.canBurrow() && this.onGround() && EntityUtilities.couldEntityFit(this, x + 0.5D, y, z + 0.5D)) {
            this.burrowX = x;
            this.burrowY = y;
            this.burrowZ = z;
            this.setBurrowProgress(1);
            this.getNavigation().stop();
            this.setDeltaMovement(0.0D, 0.0D, 0.0D);
            this.burrowCooldown = BURROW_COOLDOWN;
        }
    }

    public boolean canBurrow() {
        return this.burrowCooldown == 0;
    }

    @Override
    public boolean isInvisible() {
        return super.isInvisible() || this.isThingUnderground();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getBurrowProgress() > 0 || (this.isThingFrozen() && source == this.level().damageSources().inWall())){
            return false;
        }
        if (source.getEntity() != null && EntityUtilities.isThingAlly(source.getEntity())){
            return false;
        }
        if (!this.level().isClientSide()){
            if (source.getEntity() instanceof LivingEntity e){
                if (EntityUtilities.isVulnerable(this) && this.faw$getAggression() != Aggression.HIDING)
                    EntityUtilities.angerNearbyThings(10, this, e);
                this.currentThreat = e;
                if (this.canAttack(e)){
                    this.setTarget(e);
                }
            }
            else{
                if (EntityUtilities.isVulnerable(this) && this.faw$getAggression() != Aggression.HIDING)
                    EntityUtilities.angerNearbyThings(10, this, null);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect());
        if (id != null && Config.EFFECT_CONFIG.thingImmune.contains(id.toString())){
            return false;
        }
        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        boolean vul1 = EntityUtilities.isVulnerable(this);
        boolean vul2 = source.is(DamageTypeTags.ALWAYS_HURTS_THINGS);
        return (vul1 || vul2) ? super.getDamageAfterMagicAbsorb(source, amount) : super.getDamageAfterMagicAbsorb(source, amount) * this.getThingCategory().getDamageMultiplierWhenNotBurning();
    }

    public boolean shouldMergeOnAssimilate() {
        return false;
    }

    public boolean deathsCountForDirector() {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (EntityUtilities.assimilate(target, this.shouldMergeOnAssimilate() ? 5 : 1)){
            target.hurt(this.level().damageSources().mobAttack(this), 0.0F);
            if (this.shouldMergeOnAssimilate()){
                this.discard();
            }
            return false;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (Config.BLOCK_CONFIG.corpsesEnabled.get()) {
            this.attemptPlaceCorpse();
        }
        if (this.deathsCountForDirector()) {
            this.faw$getDirector().ifPresent(ThingBaseOfOperations.AIDirector::threaten);

            if (this.lastTunnelExit != null && this.level().getRandom().nextInt(8) == 0) {
                BlockEntity blockEntity = this.level().getBlockEntity(this.lastTunnelExit);
                if (blockEntity instanceof TunnelBlockEntity tunnelBlockEntity) {
                    tunnelBlockEntity.deactivate();
                }
                else {
                    this.lastTunnelExit = null;
                }
            }
        }
    }

    public void attemptPlaceCorpse(){
        if (!this.level().isClientSide() &&
                this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) &&
                BlockRegistry.CORPSE.get().defaultBlockState().canSurvive(this.level(), this.blockPosition()) &&
                this.level().getBlockState(this.blockPosition()).canBeReplaced()){

            FluidState fluidState = this.level().getBlockState(this.blockPosition()).getFluidState();

            this.getSuitableCorpse().ifPresentOrElse(corpseType -> {
                if (fluidState.isEmpty() || fluidState.is(Fluids.WATER)) {
                    this.level().setBlockAndUpdate(this.blockPosition(),
                            CorpseBlock.setCorpseType(BlockRegistry.CORPSE.get()
                                    .defaultBlockState().rotate(Rotation.getRandom(this.getRandom())), corpseType).setValue(CorpseBlock.WATERLOGGED, fluidState.is(Fluids.WATER)));
                }
            }, () -> {
                if (fluidState.isEmpty()) {
                    this.level().setBlockAndUpdate(this.blockPosition(), BlockRegistry.THING_GORE.get().defaultBlockState());
                }
            });
        }
    }

    public Optional<CorpseBlock.CorpseType> getSuitableCorpse(){
        return Optional.of(CorpseBlock.CorpseType.MEDIUM_1);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private void setRareAbilities(int chanceDenominator){
        this.canSpit = random.nextInt(chanceDenominator) == 0;
        //this.canHunt = random.nextInt(chanceDenominator) == 0;
        this.canGrief = random.nextInt(chanceDenominator) == 0;
        this.canShootNeedles = random.nextInt(chanceDenominator) == 0;
    }

    private void tickFreeze(){
        float random = 0.5F + this.getRandom().nextFloat();
        if (this.isOnFire()){
            this.setCold(this.getCold() - 0.2F * random);
            return;
        }
        if (this.level().getBlockState(this.blockPosition()).is(BlockTags.FREEZES_THINGS)){
            this.setCold(this.getCold() + 0.1F * random);
            return;
        }
        BlockPos blockPos = this.blockPosition();
        if (this.hasSnow(blockPos) || this.hasSnow(BlockPos.containing(blockPos.getX(), this.getBoundingBox().maxY, blockPos.getZ()))){
            this.setCold(this.getCold() + 0.01F * random);
            return;
        }
        this.setCold(this.getCold() - 0.01F * random);
    }

    private boolean hasSnow(BlockPos blockPos){
        if (!this.level().isRaining()) {
            return false;
        } else if (!this.level().canSeeSky(blockPos)) {
            return false;
        } else if (this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
            return false;
        } else {
            Biome biome = this.level().getBiome(blockPos).value();
            return biome.getPrecipitationAt(blockPos) == Biome.Precipitation.SNOW;
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public boolean canThingFreeze(){
        return true;
    }

    public boolean isThingFrozen(){
        return this.getCold() > 0.8F;
    }

    public boolean cannotMerge(){
        return false;
    }

    @Override
    public void playAmbientSound() {
        if (!this.isThingFrozen())
            super.playAmbientSound();
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || this.isThingFrozen() || this.getBurrowProgress() > 0;
    }

    @Override
    protected void jumpFromGround() {
        if (!this.hibernating())
            super.jumpFromGround();
    }

    public static boolean checkThingSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource){
        return ServerUtilities.getDayReal(serverLevelAccessor) >= Config.SPAWNING_CONFIG.firstSpawningDay.get() &&
                Monster.checkMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) &&
                randomSource.nextFloat() >= Config.SPAWNING_CONFIG.failureChance.get();
    }

    private @Nullable BlockPos lastTunnelExit = null;

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("CanSpit", this.canSpit);
        nbt.putBoolean("CanHunt", this.canHunt);
        nbt.putBoolean("CanGrief", this.canGrief);
        nbt.putBoolean("CanShootNeedles", this.canShootNeedles);

        nbt.putInt("VariantID", this.getVariantID());

        nbt.putBoolean("Hibernating", this.hibernating());
        nbt.putInt("TimeSinceLastSeenTarget", this.timeSinceLastSeenTarget);
        nbt.putFloat("Cold", this.getCold());

        if (this.isNoAi()) {
            nbt.putBoolean("NoAI", super.isNoAi());
        }

        if (this.victim != null) {
            nbt.put("Victim", this.victim);
        }

        if (this.getBurrowType() == BurrowType.REQUIRES_TUNNEL && this.lastTunnelExit != null) {
            nbt.putInt("LastTunnelExitX", this.lastTunnelExit.getX());
            nbt.putInt("LastTunnelExitY", this.lastTunnelExit.getY());
            nbt.putInt("LastTunnelExitZ", this.lastTunnelExit.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.canSpit = nbt.getBoolean("CanSpit");
        this.canHunt = nbt.getBoolean("CanHunt");
        this.canGrief = nbt.getBoolean("CanGrief");
        this.canShootNeedles = nbt.getBoolean("CanShootNeedles");

        this.setVariantID(nbt.getByte("VariantID"));

        this.setHibernating(nbt.getBoolean("Hibernating"));
        this.timeSinceLastSeenTarget = nbt.getInt("TimeSinceLastSeenTarget");
        this.setCold(nbt.getFloat("Cold"));

        if (nbt.contains("Victim")) {
            this.victim = nbt.getCompound("Victim");
        }

        if (this.getBurrowType() == BurrowType.REQUIRES_TUNNEL) {
            this.lastTunnelExit = new BlockPos(nbt.getInt("LastTunnelExitX"), nbt.getInt("LastTunnelExitY"), nbt.getInt("LastTunnelExitZ"));
        }
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return this.isThingFrozen() ? 0.0F : super.getSoundVolume();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public double animationSpeed(AnimationState<? extends Thing> state){
        return 1.0D;
    }

    @Override
    public boolean faw$isThing() {
        return true;
    }

    public enum BurrowType {
        CANNOT_BURROW,
        REQUIRES_TUNNEL,
        CAN_BURROW
    }

    public BurrowType getBurrowType() {
        return Config.DIFFICULTY_CONFIG.burrowing.get() ? BurrowType.CAN_BURROW : BurrowType.REQUIRES_TUNNEL;
    }

    public float getBurrowDepth() {
        return this.getBbHeight() + 0.5F;
    }

    public enum ThingCategory {
        REVEALED(false,
                Config.DIFFICULTY_CONFIG.revealedDamageMultiplier,
                10
        ),
        FODDER(false,
                Config.DIFFICULTY_CONFIG.fodderDamageMultiplier,
                6
        ),
        SPLIT(true,
                Config.DIFFICULTY_CONFIG.splitDamageMultiplier,
                12
        ),
        STANDARD(true,
                Config.DIFFICULTY_CONFIG.standardDamageMultiplier,
                16
        ),
        ELITE(true,
                Config.DIFFICULTY_CONFIG.eliteDamageMultiplier,
                20
        ),
        MERGED(true,
                Config.DIFFICULTY_CONFIG.mergedDamageMultiplier,
                30
        ),
        MINIBOSS(true,
                Config.DIFFICULTY_CONFIG.minibossDamageMultiplier,
                40
        ),
        SPECIAL_MINIBOSS(false,
                Config.DIFFICULTY_CONFIG.specialMinibossDamageMultiplier,
                50
        );

        ThingCategory(boolean canHaveSpecialAbilities, FloatProperty damageMultiplierWhenNotBurning, int xpReward){
            this.canHaveSpecialAbilities = canHaveSpecialAbilities;
            this.damageMultiplierWhenNotBurning = damageMultiplierWhenNotBurning;
            this.xpReward = xpReward;
        }

        private final boolean canHaveSpecialAbilities;
        private final FloatProperty damageMultiplierWhenNotBurning;
        private final int xpReward;

        public boolean canHaveSpecialAbilities(){
            return this.canHaveSpecialAbilities;
        }
        public float getDamageMultiplierWhenNotBurning(){
            return this.damageMultiplierWhenNotBurning.get();
        }
        public int getXpReward(){
            return this.xpReward;
        }
    }

    public abstract ThingCategory getThingCategory();

    static {
        VARIANT_ID = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BYTE);
        HIBERNATING = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BOOLEAN);
        COLD = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.FLOAT);
        CLIMBING = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BOOLEAN);
        BURROW_PROGRESS = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.INT);
        DISGUISE_PROGRESS = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BYTE);
    }
}
