package mod.acats.fromanotherworld.entity.misc;

import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import mod.acats.fromanotherworld.entity.thing.special.AlienThing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class StarshipEntity extends Mob implements GeoEntity {

    private static final EntityDataAccessor<Boolean> RELEASED_CONTENTS;

    public void setReleasedContents(boolean releasedContents){
        this.entityData.set(RELEASED_CONTENTS, releasedContents);
    }

    private boolean releasedContents(){
        return this.entityData.get(RELEASED_CONTENTS);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RELEASED_CONTENTS, false);
        super.defineSynchedData();
    }

    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public StarshipEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.releasedContents() || this.touchingUnloadedChunk()){
            return;
        }
        if (this.onGround()){
            if (!this.level().isClientSide()){
                this.level().explode(null, this.getX(), this.getY() + 3.0D, this.getZ(), 9, Level.ExplosionInteraction.TNT);
                AlienThing thing = EntityRegistry.ALIEN_THING.get().create(this.level());
                if (thing != null) {
                    thing.setPos(this.position());
                    thing.finalizeSpawn((ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.EVENT, null, null);
                    this.level().addFreshEntity(thing);
                    thing.changeForm(0);
                }
                this.setReleasedContents(true);
                this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 16.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            }
        }
        else if (this.level().isClientSide()){
            for(int i = 0; i < 5; ++i) {
                this.level().addParticle(ParticleRegistry.BIG_FLAMES.get(), this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) / 5, (this.random.nextDouble() - 0.5D) / 5 + this.getDeltaMovement().y, (this.random.nextDouble() - 0.5D) / 5);
                this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) / 2, ((this.random.nextDouble() - 0.5D) + this.getDeltaMovement().y) / 2, (this.random.nextDouble() - 0.5D) / 2);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.releasedContents())
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("ReleasedThings", this.releasedContents());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setReleasedContents(nbt.getBoolean("ReleasedThings"));
    }

    public static AttributeSupplier.Builder createStarshipAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.5F;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        for (int i = 0; i < 7; i++){
            BloodCrawler bloodCrawler = EntityRegistry.BLOOD_CRAWLER.get().create(this.level());
            if (bloodCrawler != null) {
                bloodCrawler.setPos(this.position().add(this.getRandom().nextFloat() - 0.5F, 0, this.getRandom().nextFloat() - 0.5F));
                this.level().addFreshEntity(bloodCrawler);
            }
        }
        super.dropAllDeathLoot(source);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.onGround()){
            return PlayState.STOP;
        }
        event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.starship.spin"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    static {
        RELEASED_CONTENTS = SynchedEntityData.defineId(StarshipEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
