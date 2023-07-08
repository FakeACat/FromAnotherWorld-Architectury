package acats.fromanotherworld.entity.thing;

import mod.azure.azurelib.core.animation.AnimationState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class ResizeableThing extends AbsorberThing{
    private static final EntityDataAccessor<Float> WIDTH_MULTIPLIER;
    private static final EntityDataAccessor<Float> HEIGHT_MULTIPLIER;
    public ResizeableThing(EntityType<? extends ResizeableThing> entityType, Level world) {
        super(entityType, world);
        this.fixupDimensions();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WIDTH_MULTIPLIER, 1.0F);
        this.entityData.define(HEIGHT_MULTIPLIER, 1.0F);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.entityData.get(WIDTH_MULTIPLIER), this.entityData.get(HEIGHT_MULTIPLIER));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (WIDTH_MULTIPLIER.equals(entityDataAccessor) || HEIGHT_MULTIPLIER.equals(entityDataAccessor)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(entityDataAccessor);
    }

    public float getOriginalWidth(){
        return super.getDimensions(this.getPose()).width;
    }

    public float getOriginalHeight(){
        return super.getDimensions(this.getPose()).height;
    }

    public float getRelativeSize(){
        return (this.entityData.get(WIDTH_MULTIPLIER) * 2 + this.entityData.get(HEIGHT_MULTIPLIER)) / 3.0F;
    }

    public float speedMultiplier(){
        return (Mth.clamp(1.0F / this.getRelativeSize(), 0.5F, 1.5F) + 4.0F) * 0.2F;
    }

    private void setScale(float scaleWidth, float scaleHeight){
        this.entityData.set(WIDTH_MULTIPLIER, scaleWidth);
        this.entityData.set(HEIGHT_MULTIPLIER, scaleHeight);
        this.reapplyPosition();
        this.refreshDimensions();
    }

    public void setupScale(float width, float height){
        float widthRatio = width / this.getOriginalWidth();
        float heightRatio = height / this.getOriginalHeight();

        this.setScale(widthRatio, heightRatio);

        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null){
            maxHealth.setBaseValue((int)(maxHealth.getValue() * this.getRelativeSize()));
            this.setHealth(this.getMaxHealth());
        }
        AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null){
            attackDamage.setBaseValue((int)(attackDamage.getValue() * this.getRelativeSize()));
        }
        AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null){
            movementSpeed.setBaseValue(movementSpeed.getValue() * this.speedMultiplier());
        }
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() / this.getRelativeSize();
    }

    @Override
    public double animationSpeed(AnimationState<? extends Thing> state) {
        return state.isMoving() ? super.animationSpeed(state) * this.speedMultiplier() : super.animationSpeed(state);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("WidthMultiplier", this.entityData.get(WIDTH_MULTIPLIER));
        nbt.putFloat("HeightMultiplier", this.entityData.get(HEIGHT_MULTIPLIER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setScale(nbt.getFloat("WidthMultiplier"), nbt.getFloat("HeightMultiplier"));
    }

    static {
        WIDTH_MULTIPLIER = SynchedEntityData.defineId(ResizeableThing.class, EntityDataSerializers.FLOAT);
        HEIGHT_MULTIPLIER = SynchedEntityData.defineId(ResizeableThing.class, EntityDataSerializers.FLOAT);
    }
}
