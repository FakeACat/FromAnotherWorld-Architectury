package acats.fromanotherworld.entity.thing;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.block.CorpseBlock;
import acats.fromanotherworld.config.General;
import acats.fromanotherworld.constants.Variants;
import acats.fromanotherworld.entity.goal.ThingTargetGoal;
import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.navigation.ThingNavigation;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.registry.SoundRegistry;
import acats.fromanotherworld.tags.BlockTags;
import acats.fromanotherworld.tags.DamageTypeTags;
import acats.fromanotherworld.tags.EntityTags;
import acats.fromanotherworld.utilities.EntityUtilities;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Thing extends Monster implements GeoEntity, MaybeThing {
    private static final EntityDataAccessor<Byte> VICTIM_TYPE;
    private static final EntityDataAccessor<Boolean> HIBERNATING;
    private static final EntityDataAccessor<Float> COLD;
    private static final EntityDataAccessor<Boolean> CLIMBING;
    protected Thing(EntityType<? extends Monster> entityType, Level world, boolean canHaveSpecialAbilities) {
        super(entityType, world);
        this.xpReward = XP_REWARD_LARGE;
        if (!this.level().isClientSide()){
            if (!entityType.is(EntityTags.THINGS)){
                FromAnotherWorld.LOGGER.error(this.getEncodeId() + " extends Thing but is not in the things tag!");
            }

            if (canHaveSpecialAbilities){
                this.setRareAbilities(General.specialBehaviourRarity);
            }
        }
    }

    public void initializeFrom(Entity parent){
        if (parent.level() instanceof ServerLevel serverWorld){
            this.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(parent.blockPosition()), MobSpawnType.CONVERSION, null, null);
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level world) {
        return new ThingNavigation(this, world);
    }

    @Override
    public float maxUpStep() {
        return 1.5F;
    }

    protected Thing(EntityType<? extends Monster> entityType, Level world){
        this(entityType, world, true);
    }

    public boolean canSpit;
    public boolean canHunt;
    public boolean canGrief;
    public boolean canShootNeedles;
    public static final int HUNTING_RANGE = 128;
    public LivingEntity currentThreat;
    private int timeSinceLastSeenTarget = 0;
    private int alertSoundCooldown = 0;
    private int climbStamina = 300;


    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public byte getVictimType(){
        return this.entityData.get(VICTIM_TYPE);
    }
    public void setVictimType(byte victimType){
        this.entityData.set(VICTIM_TYPE, victimType);
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
        return this.entityData.get(CLIMBING);
    }
    public void setClimbingWall(boolean climbingWall){
        this.entityData.set(CLIMBING, climbingWall);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VICTIM_TYPE, Variants.DEFAULT);
        this.entityData.define(HIBERNATING, false);
        this.entityData.define(COLD, 0.0F);
        this.entityData.define(CLIMBING, false);
    }

    @Override
    protected @NotNull ResourceLocation getDefaultLootTable() {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "entities/thing/the_thing_default");
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (!EntityUtilities.isThing(target) &&
                (target == this.currentThreat ||
                        EntityUtilities.canAssimilate(target) ||
                        target.getType().is(EntityTags.ATTACKABLE_BUT_NOT_ASSIMILABLE))){
            return super.canAttack(target);
        }
        return false;
    }

    public void addThingTargets(boolean prioritisePlayer){
        this.targetSelector.addGoal(prioritisePlayer ? 0 : 1, new ThingTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    public int timeUntilBored(){
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
        return this.onClimbable() && !this.isColliding(this.blockPosition(), this.level().getBlockState(this.blockPosition().above()));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (this.getFormStrength()){
            case WEAK, STANDARD_WEAK -> SoundRegistry.WEAK_HURT.get();
            case MINIBOSS -> SoundRegistry.STRONG_HURT.get();
            default -> SoundRegistry.GENERAL_HURT.get();
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return switch (this.getFormStrength()){
            case WEAK, STANDARD_WEAK -> SoundRegistry.WEAK_DEATH.get();
            case MINIBOSS -> SoundRegistry.STRONG_DEATH.get();
            default -> SoundRegistry.GENERAL_DEATH.get();
        };
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return switch (this.getFormStrength()){
            case WEAK, STANDARD_WEAK -> SoundRegistry.WEAK_AMBIENT.get();
            case MINIBOSS -> SoundRegistry.STRONG_AMBIENT.get();
            default -> SoundRegistry.GENERAL_AMBIENT.get();
        };
    }

    protected SoundEvent getAlertSound(){
        return switch (this.getFormStrength()){
            case WEAK, STANDARD_WEAK -> SoundRegistry.WEAK_ALERT.get();
            case MINIBOSS -> SoundRegistry.STRONG_ALERT.get();
            default -> null;
        };
    }

    @Override
    public float getVoicePitch() {
        return switch (this.getFormStrength()){
            default -> super.getVoicePitch();
            case STANDARD_STRONG -> super.getVoicePitch() * 0.8F;
            case STRONG -> super.getVoicePitch() * 0.6F;
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()){
            if (this.canClimb()){
                if (--this.climbStamina > 0) {
                    this.setClimbingWall(this.horizontalCollision);
                }
                else{
                    this.setClimbingWall(false);
                }
                if (this.onGround()) {
                    this.climbStamina = 300;
                }
            }
            if (this.tickCount % 10 == 0 && !EntityUtilities.isVulnerable(this)){
                this.heal(1.0F);
            }

            if (this.tickCount % 60 == 0){
                if (this.canThingFreeze())
                    this.tickFreeze();

                if (this.canGrief && this.onClimbable())
                    this.grief(1, 1);

                if (this.getTarget() == null){
                    this.timeSinceLastSeenTarget++;
                    if (timeSinceLastSeenTarget > this.timeUntilBored()){
                        this.bored();
                        this.timeSinceLastSeenTarget = 0;
                    }
                }
                else{
                    this.timeSinceLastSeenTarget = 0;
                    if (this.canGrief && !this.isNoAi()) {
                        this.grief(this.getTarget().getY() < this.getY() - 3 ? -1 : 0, 3);
                    }
                    if (this.canShootNeedles && !this.isNoAi() && this.tickCount % 300 == 0){
                        for (int i = 0; i < 50; i++){
                            NeedleEntity needleEntity = new NeedleEntity(this.level(), this.getX(), this.getRandomY(), this.getZ(), this);
                            needleEntity.setDeltaMovement(new Vec3((random.nextDouble() - 0.5D) * 5, random.nextDouble() / 2, (random.nextDouble() - 0.5D) * 5));
                            this.level().addFreshEntity(needleEntity);
                        }
                    }
                }
            }

            if (this.canShootNeedles && !this.isNoAi() && this.tickCount % 300 == 240){
                this.playSound(SoundRegistry.STRONG_AMBIENT.get(), 1.0F, 0.4F);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 6, false, false));
            }

            if (this.hibernating()){
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                if (this.getTarget() != null)
                    this.setHibernating(false);
            }
        }
        else {
            if (this.rotateWhenClimbing()){
                if (this.onClimbable()) {
                    this.climbRotateProgress = this.nextClimbRotateProgress;
                    this.nextClimbRotateProgress = Math.min(this.climbRotateProgress + 0.05F, 1.0F);
                }
                else {
                    this.climbRotateProgress = this.nextClimbRotateProgress;
                    this.nextClimbRotateProgress = Math.max(this.climbRotateProgress - 0.1F, 0.0F);
                }
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

    public void grief(int yOffset, int chanceDenominator){
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        int l = Mth.floor(this.getY()) + yOffset;
        int m = Mth.floor(this.getX());
        int n = Mth.floor(this.getZ());

        int size = Mth.floor(1 + this.getDimensions(this.getPose()).width / 2);

        for(int o = -size; o <= size; ++o) {
            for(int p = -size; p <= size; ++p) {
                for(int q = 0; q <= size + 2; ++q) {
                    int r = m + o;
                    int s = l + q;
                    int t = n + p;
                    BlockPos blockPos = new BlockPos(r, s, t);
                    BlockState blockState = this.level().getBlockState(blockPos);
                    if (EntityUtilities.canThingDestroy(blockState) && random.nextInt(chanceDenominator) == 0) {
                        this.level().destroyBlock(blockPos, true, this);
                    }
                }
            }
        }
    }

    public void bored(){

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide()){
            if (source.getEntity() instanceof LivingEntity e){
                if (EntityUtilities.isVulnerable(this))
                    EntityUtilities.angerNearbyThings(10, this, e);
                this.currentThreat = e;
                if (this.canAttack(e)){
                    this.setTarget(e);
                }
            }
            else{
                if (EntityUtilities.isVulnerable(this))
                    EntityUtilities.angerNearbyThings(10, this, null);
            }
        }
        if (this.isThingFrozen() && source == this.level().damageSources().inWall()){
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        boolean vul1 = EntityUtilities.isVulnerable(this);
        boolean vul2 = source.is(DamageTypeTags.ALWAYS_HURTS_THINGS);
        return (vul1 || vul2) ? super.getDamageAfterMagicAbsorb(source, amount) : Math.min(super.getDamageAfterMagicAbsorb(source, amount), 1.0F);
    }

    public boolean shouldMergeOnAssimilate() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (EntityUtilities.assimilate(target, this.shouldMergeOnAssimilate() ? 10 : 1)){
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
        this.attemptPlaceCorpse();
    }

    public void attemptPlaceCorpse(){
        if (!this.level().isClientSide() &&
                this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) &&
                BlockRegistry.CORPSE.get().defaultBlockState().canSurvive(this.level(), this.blockPosition()) &&
                this.level().getBlockState(this.blockPosition()).canBeReplaced() &&
                this.level().getBlockState(this.blockPosition()).getFluidState().isEmpty()){

            CorpseBlock.CorpseType corpseType = this.getSuitableCorpse();

            if (corpseType == null){
                this.level().setBlockAndUpdate(this.blockPosition(), BlockRegistry.THING_GORE.get().defaultBlockState());
                return;
            }

            BlockState corpse = BlockRegistry.CORPSE.get().defaultBlockState();
            CorpseBlock.setCorpseType(corpse, corpseType);


            this.level().setBlockAndUpdate(this.blockPosition(), corpse);
        }
    }

    @Nullable
    public CorpseBlock.CorpseType getSuitableCorpse(){
        return CorpseBlock.CorpseType.HUMAN_1;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private void setRareAbilities(int chanceDenominator){
        this.canSpit = random.nextInt(chanceDenominator) == 0;
        this.canHunt = random.nextInt(chanceDenominator) == 0;
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
        return super.isNoAi() || this.isThingFrozen();
    }

    @Override
    protected void jumpFromGround() {
        if (!this.hibernating())
            super.jumpFromGround();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("CanSpit", this.canSpit);
        nbt.putBoolean("CanHunt", this.canHunt);
        nbt.putBoolean("CanGrief", this.canGrief);
        nbt.putBoolean("CanShootNeedles", this.canShootNeedles);

        nbt.putInt("VictimType", this.getVictimType());

        nbt.putBoolean("Hibernating", this.hibernating());
        nbt.putInt("TimeSinceLastSeenTarget", this.timeSinceLastSeenTarget);
        nbt.putFloat("Cold", this.getCold());

        if (this.isNoAi()) {
            nbt.putBoolean("NoAI", super.isNoAi());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.canSpit = nbt.getBoolean("CanSpit");
        this.canHunt = nbt.getBoolean("CanHunt");
        this.canGrief = nbt.getBoolean("CanGrief");
        this.canShootNeedles = nbt.getBoolean("CanShootNeedles");

        this.setVictimType(nbt.getByte("VictimType"));

        this.setHibernating(nbt.getBoolean("Hibernating"));
        this.timeSinceLastSeenTarget = nbt.getInt("TimeSinceLastSeenTarget");
        this.setCold(nbt.getFloat("Cold"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public double animationSpeed(AnimationState<? extends Thing> state){
        return 1.0D;
    }

    @Override
    public boolean isThing() {
        return true;
    }

    public enum Strength {
        REVEALED,
        WEAK,
        STANDARD_WEAK,
        STANDARD,
        STANDARD_STRONG,
        STRONG,
        MINIBOSS
    }

    public abstract Strength getFormStrength();

    static {
        VICTIM_TYPE = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BYTE);
        HIBERNATING = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BOOLEAN);
        COLD = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.FLOAT);
        CLIMBING = SynchedEntityData.defineId(Thing.class, EntityDataSerializers.BOOLEAN);
    }
}
