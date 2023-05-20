package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.MergeGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class CrawlerEntity extends AbstractThingEntity {

    public CrawlerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new MergeGoal(this, EntityRegistry.BLAIR_THING.get()));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
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

    public static DefaultAttributeContainer.Builder createCrawlerAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void bored() {
        if (!this.mergeCore)
            this.setHibernating(true);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD_WEAK;
    }
}
