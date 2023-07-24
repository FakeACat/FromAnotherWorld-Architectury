package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.entity.thing.AbsorberThing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class MinibossThing extends AbsorberThing {
    private static final EntityDataAccessor<Integer> TIER;

    public MinibossThing(EntityType<? extends AbsorberThing> entityType, Level world) {
        super(entityType, world);
        this.fixupDimensions();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TIER, 0);
    }

    abstract double getStartingHealth();
    abstract double getScalingHealth();
    abstract double getStartingSpeed();
    abstract double getScalingSpeed();
    abstract double getStartingDamage();
    abstract double getScalingDamage();

    public void setTier(int tier, boolean heal){
        this.entityData.set(TIER, tier);
        this.reapplyPosition();
        this.refreshDimensions();
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(this.getStartingHealth() + this.getScalingHealth() * tier);
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(this.getStartingSpeed() + this.getScalingSpeed() * tier);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(this.getStartingDamage() + this.getScalingDamage() * tier);

        if (heal){
            this.setHealth(this.getMaxHealth());
        }

        this.xpReward = tier * this.getThingCategory().getXpReward();
    }

    public int getTier() {
        return this.entityData.get(TIER);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Tier", this.getTier());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setTier(nbt.getInt("Tier"), false);
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();
        this.setPos(d, e, f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (TIER.equals(data)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(data);
    }

    public float scaleFactor(){
        return 1.0F + 0.25F * (float)this.getTier();
    }

    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.scaleFactor());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityNbt) {
        this.setTier(this.getTier(), true);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.MINIBOSS;
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.setTier(this.getTier() + 1, true);
    }

    @Override
    public boolean cannotMerge() {
        return this.getTier() >= Config.DIFFICULTY_CONFIG.maxMinibossTier.get();
    }

    @Override
    public boolean canDisguise() {
        return super.canDisguise() && this.getTier() == 0;
    }

    static {
        TIER = SynchedEntityData.defineId(MinibossThing.class, EntityDataSerializers.INT);
    }
}
