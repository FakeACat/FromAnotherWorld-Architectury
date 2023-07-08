package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.block.CorpseBlock;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.model.thing.resultant.BloodCrawlerModel;
import acats.fromanotherworld.entity.thing.Thing;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BloodCrawler extends Thing {

    private static final EntityDataAccessor<Integer> VARIANT;
    public BloodCrawler(EntityType<? extends BloodCrawler> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.0, 1.2));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, IronGolem.class, 16.0F, 1.0, 1.2));
        this.goalSelector.addGoal(1, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D, 1));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if ((event.isMoving() || this.movingClimbing()) && !this.isThingFrozen()) {
            event.getController().setAnimationSpeed(2.0D);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation." + BloodCrawlerModel.getVariant(this) + ".crawl"));
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

    public static AttributeSupplier.Builder createBloodCrawlerAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.MAX_HEALTH, 1.0D);
    }

    @Override
    public boolean shouldMergeOnAssimilate() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, random.nextInt(2));
    }

    public void setVariant(int variant){
        this.entityData.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(nbt.getInt("Variant"));
    }

    @Nullable
    @Override
    public CorpseBlock.CorpseType getSuitableCorpse() {
        return null;
    }

    static {
        VARIANT = SynchedEntityData.defineId(BloodCrawler.class, EntityDataSerializers.INT);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.FODDER;
    }
}
