package acats.fromanotherworld.entity.misc;

import acats.fromanotherworld.entity.thing.ThingEntity;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StarshipEntity extends MobEntity implements GeoEntity {

    private static final TrackedData<Boolean> RELEASED_CONTENTS;

    public void setReleasedContents(boolean releasedContents){
        this.dataTracker.set(RELEASED_CONTENTS, releasedContents);
    }

    private boolean releasedContents(){
        return this.dataTracker.get(RELEASED_CONTENTS);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(RELEASED_CONTENTS, false);
        super.initDataTracker();
    }

    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);

    public StarshipEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.releasedContents() || this.isRegionUnloaded()){
            return;
        }
        if (this.isOnGround()){
            if (!this.world.isClient()){
                this.world.createExplosion(null, this.getX(), this.getY() + 3.0D, this.getZ(), 9, World.ExplosionSourceType.TNT);
                ThingEntity thing = EntityRegistry.ALIEN_THING.get().create(this.world);
                if (thing != null) {
                    thing.setPosition(this.getPos());
                    this.world.spawnEntity(thing);
                }
                this.setReleasedContents(true);
                this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 16.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
            }
        }
        else if (this.world.isClient()){
            for(int i = 0; i < 5; ++i) {
                this.world.addParticle(ParticleRegistry.BIG_FLAMES, this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) / 5, (this.random.nextDouble() - 0.5D) / 5 + this.getVelocity().y, (this.random.nextDouble() - 0.5D) / 5);
                this.world.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) / 2, ((this.random.nextDouble() - 0.5D) + this.getVelocity().y) / 2, (this.random.nextDouble() - 0.5D) / 2);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.releasedContents())
            return false;
        return super.damage(source, amount);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("ReleasedThings", this.releasedContents());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setReleasedContents(nbt.getBoolean("ReleasedThings"));
    }

    public static DefaultAttributeContainer.Builder createStarshipAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    public float getSoundPitch() {
        return 0.5F;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    protected void drop(DamageSource source) {
        for (int i = 0; i < 7; i++){
            BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(this.world);
            if (bloodCrawlerEntity != null) {
                bloodCrawlerEntity.setPosition(this.getPos().add(this.getRandom().nextFloat() - 0.5F, 0, this.getRandom().nextFloat() - 0.5F));
                this.world.spawnEntity(bloodCrawlerEntity);
            }
        }
        super.drop(source);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isOnGround()){
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
        RELEASED_CONTENTS = DataTracker.registerData(StarshipEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
