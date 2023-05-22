package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.model.thing.resultant.BloodCrawlerEntityModel;
import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.registry.BlockRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class BloodCrawlerEntity extends AbstractThingEntity {

    private static final TrackedData<Integer> VARIANT;
    public BloodCrawlerEntity(EntityType<? extends BloodCrawlerEntity> entityType, World world) {
        super(entityType, world, false);
        this.experiencePoints = SMALL_MONSTER_XP;
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeEntityGoal<>(this, PlayerEntity.class, 16.0F, 1.0, 1.2));
        this.goalSelector.add(0, new FleeEntityGoal<>(this, IronGolemEntity.class, 16.0F, 1.0, 1.2));
        this.goalSelector.add(1, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0D, 1));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if ((event.isMoving() || this.movingClimbing()) && !this.isThingFrozen()) {
            event.getController().setAnimationSpeed(2.0D);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation." + BloodCrawlerEntityModel.getVariant(this) + ".crawl"));
        }
        else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }
    @Override
    public float offsetWhenClimbing() {
        return -0.25F;
    }

    public static DefaultAttributeContainer.Builder createBloodCrawlerAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D);
    }

    @Override
    public boolean shouldMergeOnAssimilate() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, random.nextInt(2));
    }

    public void setVariant(int variant){
        this.dataTracker.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(nbt.getInt("Variant"));
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        BlockPos p = BlockPos.ofFloored(this.getX(), this.getY(), this.getZ());
        if (!world.isClient && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && BlockRegistry.THING_GORE.get().getDefaultState().canPlaceAt(world, p) && world.getBlockState(p).isReplaceable() && world.getBlockState(p).getFluidState().isEmpty()){
            world.setBlockState(p, BlockRegistry.THING_GORE.get().getDefaultState());
        }
        super.onDeathWithoutGoreDrops(damageSource);
    }

    static {
        VARIANT = DataTracker.registerData(BloodCrawlerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public Strength getFormStrength() {
        return Strength.WEAK;
    }
}
