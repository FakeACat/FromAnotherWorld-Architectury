package acats.fromanotherworld.entity.thing.resultant;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractMinibossThingEntity extends AbstractAbsorberThingEntity {
    private static final TrackedData<Integer> TIER;

    public AbstractMinibossThingEntity(EntityType<? extends AbstractAbsorberThingEntity> entityType, World world) {
        super(entityType, world, true);
        this.reinitDimensions();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TIER, 0);
    }

    abstract double getStartingHealth();
    abstract double getScalingHealth();
    abstract double getStartingSpeed();
    abstract double getScalingSpeed();
    abstract double getStartingDamage();
    abstract double getScalingDamage();

    public void setTier(int tier, boolean heal){
        this.dataTracker.set(TIER, tier);
        this.refreshPosition();
        this.calculateDimensions();
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(this.getStartingHealth() + this.getScalingHealth() * tier);
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(this.getStartingSpeed() + this.getScalingSpeed() * tier);
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).setBaseValue(this.getStartingDamage() + this.getScalingDamage() * tier);

        if (heal){
            this.setHealth(this.getMaxHealth());
        }

        this.experiencePoints = tier * STRONGER_MONSTER_XP;
    }

    public int getTier() {
        return this.dataTracker.get(TIER);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Tier", this.getTier());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setTier(nbt.getInt("Tier"), false);
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (TIER.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(1.0F + 0.25F * (float)this.getTier());
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setTier(this.getTier(), true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public Strength getFormStrength() {
        return Strength.MINIBOSS;
    }

    public static void minibossGrow(AbstractMinibossThingEntity entity){
        entity.setTier(entity.getTier() + 1, true);
    }

    @Override
    public boolean cannotMerge() {
        return this.getTier() >= 2;
    }

    static {
        TIER = DataTracker.registerData(AbstractMinibossThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
