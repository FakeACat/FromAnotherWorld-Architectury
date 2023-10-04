package mod.acats.fromanotherworld.entity.thing.revealed;

import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.entity.interfaces.MaybeThing;
import mod.acats.fromanotherworld.registry.SoundRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VineTentacles extends LivingEntity implements MaybeThing, GeoEntity {
    private static final EntityDataAccessor<Integer> EXIT_PROGRESS;
    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);
    public VineTentacles(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public int getExitProgress() {
        return this.entityData.get(EXIT_PROGRESS);
    }

    public void setExitProgress(int exitProgress) {
        this.entityData.set(EXIT_PROGRESS, exitProgress);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(EXIT_PROGRESS, -1);
    }

    @Override
    public boolean faw$isThing() {
        return true;
    }

    @Override
    public boolean faw$isDistinctThing() {
        return false;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public static AttributeSupplier.Builder createVineTentaclesAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double d, double e, double f) {
    }

    public static final double RANGE_SQ = 4.0D;
    public @Nullable Entity victim;
    private int clientExitProgress;
    private int oldClientExitProgress;
    public float lerpedExitProgress(float partialTick) {
        return Mth.lerp(partialTick, oldClientExitProgress, clientExitProgress);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.leaving()) {
                this.oldClientExitProgress = this.clientExitProgress;
                this.clientExitProgress = this.getExitProgress();
            }
        }
        else {
            if (this.leaving()) {
                this.setExitProgress(this.getExitProgress() + 1);
                if (this.getExitProgress() > 20) {
                    this.discard();
                }
            }
            else {
                if (this.victim == null || this.victim.distanceToSqr(this) > RANGE_SQ || !this.victim.isAlive() || EntityUtilities.isThing(this.victim)) {
                    this.leave();
                    return;
                }

                if (this.tickCount % 15 == 0) {
                    if (EntityUtilities.assimilate(this.victim, 0.25F)) {
                        this.victim.hurt(this.level().damageSources().mobAttack(this), 0.0F);
                    }
                    else {
                        this.victim.hurt(this.level().damageSources().mobAttack(this), 3.0F);
                    }
                }

                this.victim.setDeltaMovement(this.getX() - this.victim.getX(), this.victim.getDeltaMovement().y(), this.getZ() - this.victim.getZ());
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.GENERAL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.GENERAL_DEATH.get();
    }

    private void leave() {
        if (!this.leaving()) {
            this.setExitProgress(0);
        }
    }

    private boolean leaving() {
        return this.getExitProgress() != -1;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        this.leave();
        return super.hurt(damageSource, f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        event.getController().setAnimation(FAWAnimations.EMERGE);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    static {
        EXIT_PROGRESS = SynchedEntityData.defineId(VineTentacles.class, EntityDataSerializers.INT);
    }
}
