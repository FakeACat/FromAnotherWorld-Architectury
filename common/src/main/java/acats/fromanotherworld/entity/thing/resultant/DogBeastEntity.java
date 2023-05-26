package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class DogBeastEntity extends AbsorberThingEntity {

    public DogBeastEntity(EntityType<? extends DogBeastEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.0, 1.2));
        this.goalSelector.add(1, new AbsorbGoal(this,
                STANDARD,
                (livingEntity) -> defaultGrow(livingEntity, EntityRegistry.BEAST.get())
        ));
        this.goalSelector.add(2, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    public static DefaultAttributeContainer.Builder createDogBeastAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D);
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (event.isMoving() || this.movingClimbing()){
            event.getController().setAnimationSpeed(2.0D);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dogbeast.walk"));
        }
        else{
            event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dogbeast.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (random.nextInt(3) == 0){
            DogBeastSpitterEntity dogSpitterEntity = EntityRegistry.DOGBEAST_SPITTER.get().create(this.world);
            if (dogSpitterEntity != null) {
                dogSpitterEntity.setPosition(this.getPos());
                this.world.spawnEntity(dogSpitterEntity);
            }
        }
        super.onDeath(source);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD;
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }
}
