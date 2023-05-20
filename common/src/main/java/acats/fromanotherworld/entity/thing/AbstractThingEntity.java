package acats.fromanotherworld.entity.thing;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.General;
import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.ThingTargetGoal;
import acats.fromanotherworld.entity.interfaces.VariableThing;
import acats.fromanotherworld.entity.navigation.ThingNavigation;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.registry.SoundRegistry;
import acats.fromanotherworld.tags.BlockTags;
import acats.fromanotherworld.tags.DamageTypeTags;
import acats.fromanotherworld.tags.EntityTags;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractThingEntity extends HostileEntity implements GeoEntity, VariableThing {
    private static final TrackedData<Integer> MERGED_THINGS;
    private static final TrackedData<Integer> MERGE_TIMER;
    private static final TrackedData<Integer> MERGE_CORE_ID;
    private static final TrackedData<Integer> VICTIM_TYPE;
    private static final TrackedData<Boolean> HIBERNATING;
    private static final TrackedData<Float> COLD;
    private static final TrackedData<Boolean> CLIMBING;
    protected AbstractThingEntity(EntityType<? extends HostileEntity> entityType, World world, boolean canHaveSpecialAbilities) {
        super(entityType, world);
        this.experiencePoints = STRONG_MONSTER_XP;
        if (!this.getWorld().isClient()){
            if (!entityType.isIn(EntityTags.THINGS)){
                FromAnotherWorld.LOGGER.error(this.getSavedEntityId() + " extends AbstractThingEntity but is not in the things tag!");
            }

            if (canHaveSpecialAbilities){
                this.setRareAbilities(General.specialBehaviourRarity);
            }
            if (this.canMerge()){
                this.mergeCore = random.nextInt(10) == 0;
            }
        }
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new ThingNavigation(this, world);
    }

    protected AbstractThingEntity(EntityType<? extends HostileEntity> entityType, World world){
        this(entityType, world, true);
    }

    public boolean canSpit;
    public boolean canHunt;
    public boolean canGrief;
    public boolean canShootNeedles;
    public static final int HUNTING_RANGE = 256;
    public boolean mergeCore;
    public LivingEntity currentThreat;
    private int timeSinceLastSeenTarget = 0;
    private int alertSoundCooldown = 0;
    private int climbStamina = 300;


    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public int getMergedThings() {
        return this.dataTracker.get(MERGED_THINGS);
    }
    public void setMergedThings(int mergedThings){
        this.dataTracker.set(MERGED_THINGS, mergedThings);
    }

    public int getMergeTimer() {
        return this.dataTracker.get(MERGE_TIMER);
    }
    public void setMergeTimer(int mergeTimer){
        this.dataTracker.set(MERGE_TIMER, mergeTimer);
    }

    public int getMergeCoreID() {
        return this.dataTracker.get(MERGE_CORE_ID);
    }
    public void setMergeCoreID(int mergeCoreID){
        this.dataTracker.set(MERGE_CORE_ID, mergeCoreID);
    }

    public int getVictimType(){
        return this.dataTracker.get(VICTIM_TYPE);
    }
    public void setVictimType(int victimType){
        this.dataTracker.set(VICTIM_TYPE, victimType);
    }

    public boolean hibernating(){
        return this.dataTracker.get(HIBERNATING);
    }
    public void setHibernating(boolean asleep){
        this.dataTracker.set(HIBERNATING, asleep);
    }

    public float getCold(){
        return this.dataTracker.get(COLD);
    }
    public void setCold(float cold){
        this.dataTracker.set(COLD, MathHelper.clamp(cold, 0.0F, 1.0F));
    }

    public boolean isClimbingWall(){
        return this.dataTracker.get(CLIMBING);
    }
    public void setClimbingWall(boolean climbingWall){
        this.dataTracker.set(CLIMBING, climbingWall);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MERGED_THINGS, 0);
        this.dataTracker.startTracking(MERGE_TIMER, 0);
        this.dataTracker.startTracking(MERGE_CORE_ID, 0);
        this.dataTracker.startTracking(VICTIM_TYPE, -1);
        this.dataTracker.startTracking(HIBERNATING, false);
        this.dataTracker.startTracking(COLD, 0.0F);
        this.dataTracker.startTracking(CLIMBING, false);
    }

    @Override
    protected Identifier getLootTableId() {
        return new Identifier(FromAnotherWorld.MOD_ID, "entities/thing/the_thing_default");
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if (!FromAnotherWorld.isThing(target) &&
                (target == this.currentThreat ||
                        FromAnotherWorld.canAssimilate(target) ||
                        target.getType().isIn(EntityTags.ATTACKABLE_BUT_NOT_ASSIMILABLE))){
            return super.canTarget(target);
        }
        return false;
    }

    public void addThingTargets(boolean prioritisePlayer){
        this.targetSelector.add(prioritisePlayer ? 0 : 1, new ThingTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, true));
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public boolean shouldBeCounted(){
        return true;
    }

    public int timeUntilBored(){
        return 100;
    }

    public boolean canClimb(){
        return true;
    }

    @Override
    public boolean isClimbing() {
        return this.isClimbingWall() || super.isClimbing();
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
        return this.isClimbing() && !this.collidesWithStateAtPos(this.getBlockPos(), this.getWorld().getBlockState(this.getBlockPos().up()));
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
    public float getSoundPitch() {
        return switch (this.getFormStrength()){
            default -> super.getSoundPitch();
            case STANDARD_STRONG -> super.getSoundPitch() * 0.8F;
            case STRONG -> super.getSoundPitch() * 0.6F;
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient()){
            if (this.canClimb()){
                if (--this.climbStamina > 0) {
                    this.setClimbingWall(this.horizontalCollision);
                }
                else{
                    this.setClimbingWall(false);
                }
                if (this.isOnGround()) {
                    this.climbStamina = 300;
                }
            }
            if ((this.age % 10 == 0 || this.getMergedThings() > 0) && !FromAnotherWorld.isVulnerable(this)){
                this.heal(1.0F);
            }

            if (this.age % 60 == 0){
                if (this.canThingFreeze())
                    this.tickFreeze();

                if (this.canGrief && this.isClimbing() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
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
                    if (this.canGrief && !this.isAiDisabled() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                        this.grief(this.getTarget().getY() < this.getY() - 3 ? -1 : 0, 3);
                    }
                    if (this.canShootNeedles && !this.isAiDisabled() && this.age % 300 == 0){
                        for (int i = 0; i < 50; i++){
                            NeedleEntity needleEntity = new NeedleEntity(world, this.getX(), this.getRandomBodyY(), this.getZ(), this);
                            needleEntity.setVelocity(new Vec3d((random.nextDouble() - 0.5D) * 5, random.nextDouble() / 2, (random.nextDouble() - 0.5D) * 5));
                            world.spawnEntity(needleEntity);
                        }
                    }
                }
            }

            if (this.canShootNeedles && !this.isAiDisabled() && this.age % 300 == 240){
                this.playSound(SoundRegistry.STRONG_AMBIENT.get(), 1.0F, 0.4F);
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 6, false, false));
            }

            if (this.hibernating()){
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
                if (this.getTarget() != null)
                    this.setHibernating(false);
            }
        }
        else {
            if (this.rotateWhenClimbing()){
                if (this.isClimbing()) {
                    this.climbRotateProgress = this.nextClimbRotateProgress;
                    this.nextClimbRotateProgress = Math.min(this.climbRotateProgress + 0.05F, 1.0F);
                }
                else {
                    this.climbRotateProgress = this.nextClimbRotateProgress;
                    this.nextClimbRotateProgress = Math.max(this.climbRotateProgress - 0.1F, 0.0F);
                }
            }
            if (!this.isThingFrozen() && this.getMergedThings() > 0) {
                for (int i = 0; i < this.getMergedThings(); i++) {
                    this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);
                }
            }
        }
        if (this.getAlertSound() != null){
            if (alertSoundCooldown > 0){
                alertSoundCooldown--;
            }
            else if (this.isAttacking()){
                alertSoundCooldown = 6000;
                this.playSound(this.getAlertSound(), this.getSoundVolume(), this.getSoundPitch());
            }
        }
        Entity mergeCoreEntity = this.world.getEntityById(this.getMergeCoreID());
        if (this.getMergeTimer() > 0){
            if (mergeCoreEntity != null && mergeCoreEntity.isAlive() && this.getMergeTimer() < 100){
                this.setMergeTimer(this.getMergeTimer() + 1);
                for(int i = 0; i < (this.getMergeTimer() == 99 ? 100 : (this.getMergeTimer() / 20)); ++i) {
                    double random = this.random.nextDouble();
                    Vec3d start = this.getPos().add(0, this.getHeight() / 2, 0);
                    Vec3d finish = mergeCoreEntity.getPos().add(0, mergeCoreEntity.getHeight() / 2, 0);
                    Vec3d pos = new Vec3d(MathHelper.lerp(random, start.x, finish.x), MathHelper.lerp(random, start.y, finish.y), MathHelper.lerp(random, start.z, finish.z));
                    this.world.addParticle(ParticleRegistry.THING_GORE, pos.x, pos.y, pos.z, 0, 0, 0);
                    this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);
                }
            }
            else{
                this.discard();
            }
        }
    }

    public void grief(int yOffset, int chanceDenominator){
        int l = MathHelper.floor(this.getY()) + yOffset;
        int m = MathHelper.floor(this.getX());
        int n = MathHelper.floor(this.getZ());

        int size = MathHelper.floor(1 + this.getDimensions(this.getPose()).width / 2);

        for(int o = -size; o <= size; ++o) {
            for(int p = -size; p <= size; ++p) {
                for(int q = 0; q <= size + 2; ++q) {
                    int r = m + o;
                    int s = l + q;
                    int t = n + p;
                    BlockPos blockPos = new BlockPos(r, s, t);
                    BlockState blockState = this.world.getBlockState(blockPos);
                    if (FromAnotherWorld.canThingDestroy(blockState) && random.nextInt(chanceDenominator) == 0) {
                        this.world.breakBlock(blockPos, true, this);
                    }
                }
            }
        }
    }

    public void bored(){

    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!world.isClient()){
            if (source.getAttacker() instanceof LivingEntity e){
                if (FromAnotherWorld.isVulnerable(this))
                    FromAnotherWorld.angerNearbyThings(10, this, e);
                this.currentThreat = e;
                if (this.canTarget(e)){
                    this.setTarget(e);
                }
            }
            else{
                if (FromAnotherWorld.isVulnerable(this))
                    FromAnotherWorld.angerNearbyThings(10, this, null);
            }
        }
        if (this.isThingFrozen() && source == this.getWorld().getDamageSources().inWall()){
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        boolean vul1 = FromAnotherWorld.isVulnerable(this);
        boolean vul2 = source.isIn(DamageTypeTags.ALWAYS_HURTS_THINGS);
        if (!vul2 && this.mergeCore && this.getMergedThings() > 0){
            amount *= Math.pow(0.8D, this.getMergedThings());
        }
        return (vul1 || vul2) ? super.modifyAppliedDamage(source, amount) : Math.min(super.modifyAppliedDamage(source, amount), 1.0F);
    }

    public boolean shouldMergeOnAssimilate() {
        return false;
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (FromAnotherWorld.assimilate(target, this.shouldMergeOnAssimilate() ? 10 : 1)){
            target.damage(this.world.getDamageSources().mobAttack(this), 0.0F);
            if (this.shouldMergeOnAssimilate()){
                this.discard();
            }
            return false;
        }
        return super.tryAttack(target);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.onDeathWithoutGoreDrops(damageSource);
        int size = (int)this.getDimensions(this.getPose()).width / 2 + 1;
        if (!world.isClient && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)){
            for(int x = (int)getX() - size; x < getX() + size; x++){
                for(int y = (int)getY(); y < (int)getY() + this.getDimensions(this.getPose()).height; y++){
                    for(int z = (int)getZ() - size; z < getZ() + size; z++){
                        if (random.nextInt(2) == 0 && BlockRegistry.THING_GORE.get().getDefaultState().canPlaceAt(world, new BlockPos(x, y, z)) && world.getBlockState(new BlockPos(x, y, z)).isReplaceable() && world.getBlockState(new BlockPos(x, y, z)).getFluidState().isEmpty()){
                            world.setBlockState(new BlockPos(x, y, z), BlockRegistry.THING_GORE.get().getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public void onDeathWithoutGoreDrops(DamageSource damageSource){
        super.onDeath(damageSource);
    }

    @Override
    public boolean canBreatheInWater() {
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
        if (this.getWorld().getBlockState(this.getBlockPos()).isIn(BlockTags.FREEZES_THINGS)){
            this.setCold(this.getCold() + 0.1F * random);
            return;
        }
        BlockPos blockPos = this.getBlockPos();
        if (this.hasSnow(blockPos) || this.hasSnow(BlockPos.ofFloored(blockPos.getX(), this.getBoundingBox().maxY, blockPos.getZ()))){
            this.setCold(this.getCold() + 0.01F * random);
            return;
        }
        this.setCold(this.getCold() - 0.01F * random);
    }

    private boolean hasSnow(BlockPos blockPos){
        if (!this.getWorld().isRaining()) {
            return false;
        } else if (!this.getWorld().isSkyVisible(blockPos)) {
            return false;
        } else if (this.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
            return false;
        } else {
            Biome biome = this.getWorld().getBiome(blockPos).value();
            return biome.getPrecipitation(blockPos) == Biome.Precipitation.SNOW;
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

    @Override
    public void playAmbientSound() {
        if (!this.isThingFrozen())
            super.playAmbientSound();
    }

    @Override
    public boolean isAiDisabled() {
        return super.isAiDisabled() || this.isThingFrozen();
    }

    @Override
    protected void jump() {
        if (!this.hibernating())
            super.jump();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("CanSpit", this.canSpit);
        nbt.putBoolean("CanHunt", this.canHunt);
        nbt.putBoolean("CanGrief", this.canGrief);
        nbt.putBoolean("CanShootNeedles", this.canShootNeedles);

        if (this.canMerge()){
            nbt.putBoolean("MergeCore", this.mergeCore);
            if (this.mergeCore){
                nbt.putInt("MergedThings", this.getMergedThings());
            }
            nbt.putInt("MergeTimer", this.getMergeTimer());
        }

        nbt.putInt("VictimType", this.getVictimType());

        nbt.putBoolean("Hibernating", this.hibernating());
        nbt.putInt("TimeSinceLastSeenTarget", this.timeSinceLastSeenTarget);
        nbt.putFloat("Cold", this.getCold());

        if (this.isAiDisabled()) {
            nbt.putBoolean("NoAI", super.isAiDisabled());
        }
    }

    public abstract boolean canMerge();

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.canSpit = nbt.getBoolean("CanSpit");
        this.canHunt = nbt.getBoolean("CanHunt");
        this.canGrief = nbt.getBoolean("CanGrief");
        this.canShootNeedles = nbt.getBoolean("CanShootNeedles");

        if (this.canMerge()){
            this.mergeCore = nbt.getBoolean("MergeCore");
            if (this.mergeCore){
                this.setMergedThings(nbt.getInt("MergedThings"));
            }
            this.setMergeTimer(nbt.getInt("MergeTimer"));
        }

        this.setVictimType(nbt.getInt("VictimType"));

        this.setHibernating(nbt.getBoolean("Hibernating"));
        this.timeSinceLastSeenTarget = nbt.getInt("TimeSinceLastSeenTarget");
        this.setCold(nbt.getFloat("Cold"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "defaultController", 5, this::defaultController));
    }

    private <E extends GeoEntity> PlayState defaultController(AnimationState<E> event){
        if (this.isThingFrozen()){
            if (this.getCold() == 1.0F){
                event.getController().setAnimation(FAWAnimations.FROZEN);
            }
            else{
                event.getController().setAnimation(FAWAnimations.FREEZING);
            }
        }
        else{
            if (event.isMoving() || (this.rotateWhenClimbing() && this.movingClimbing())){
                if (this.isAttacking()){
                    event.getController().setAnimation(FAWAnimations.CHASE);
                }
                else{
                    event.getController().setAnimation(FAWAnimations.WALK);
                }
            }
            else{
                event.getController().setAnimation(FAWAnimations.IDLE);
            }
        }
        return PlayState.CONTINUE;
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
        MERGED_THINGS = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        MERGE_TIMER = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        MERGE_CORE_ID = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        VICTIM_TYPE = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        HIBERNATING = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        COLD = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.FLOAT);
        CLIMBING = DataTracker.registerData(AbstractThingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
