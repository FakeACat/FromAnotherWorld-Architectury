package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.block.CorpseBlock;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.thing.AbsorberThing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Crawler extends AbsorberThing {

    public Crawler(EntityType<? extends Crawler> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD, Config.difficultyConfig.crawlerMergeChance.get()));
        this.goalSelector.addGoal(2, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (this.hibernating()) {
            event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.crawler.sleeping"));
        }
        else {
            if (event.isMoving() || this.movingClimbing()){
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.crawler.walk"));
                event.getController().setAnimationSpeed(1.5D);
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.crawler.idle"));
                event.getController().setAnimationSpeed(1.0D);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }
    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    public static AttributeSupplier.Builder createCrawlerAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void bored() {
        this.setHibernating(true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.SPLIT;
    }

    @Override
    public @Nullable CorpseBlock.CorpseType getSuitableCorpse() {
        return CorpseBlock.CorpseType.SMALL_1;
    }

    public boolean blairSpawned;

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("BlairSpawned", this.blairSpawned);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.blairSpawned = nbt.getBoolean("BlairSpawned");
    }

    @Override
    public boolean cannotMerge() {
        return this.blairSpawned;
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.SPLIT_FACE.get());
    }
}
