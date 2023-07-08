package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingProjectileAttackGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.thing.ResizeableThing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DogBeastSpitter extends ResizeableThing implements RangedAttackMob {

    private static final EntityDataAccessor<Boolean> ATTACKING;

    public DogBeastSpitter(EntityType<? extends DogBeastSpitter> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.PROWLER.get());
    }

    public void setHasTarget(boolean bl){
        this.entityData.set(ATTACKING, bl);
    }

    public boolean hasTarget(){
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.setHasTarget(this.getTarget() != null);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(2, new ThingProjectileAttackGoal(this, 1.0, 40, 80, 10.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    public static AttributeSupplier.Builder createDogBeastSpitterAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.175D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.MAX_HEALTH, 22.0D);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        for (int i = 0; i < (this.canSpit ? 6 : 3); i++){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.level(), this);
            assimilationLiquid.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(assimilationLiquid.position()).normalize().add(new Vec3(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).scale(this.canSpit ? 0.02 : 0.01)));
            this.level().addFreshEntity(assimilationLiquid);
        }
    }

    private AnimationController<DogBeastSpitter> spitController(){
        return new AnimationController<>(this, "SpitterSpit", 5, (event) -> {
            if (this.hasTarget() && !event.isMoving()){
                event.getController().setAnimation(FAWAnimations.SPIT);
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(FAWAnimations.defaultThingNoChase(this));
        controllerRegistrar.add(this.spitController());
    }

    static {
        ATTACKING = SynchedEntityData.defineId(DogBeastSpitter.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.SPLIT;
    }
}
