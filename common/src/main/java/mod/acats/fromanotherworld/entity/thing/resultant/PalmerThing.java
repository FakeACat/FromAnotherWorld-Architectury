package mod.acats.fromanotherworld.entity.thing.resultant;

import mod.acats.fromanotherworld.block.CorpseBlock;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.constants.VariantID;
import mod.acats.fromanotherworld.entity.goal.AbsorbGoal;
import mod.acats.fromanotherworld.entity.goal.DirectedWanderGoal;
import mod.acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import mod.acats.fromanotherworld.entity.goal.LeapAttackGoal;
import mod.acats.fromanotherworld.entity.render.thing.growths.Tentacle;
import mod.acats.fromanotherworld.entity.thing.AbsorberThing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class PalmerThing extends AbsorberThing {

    private static final EntityDataAccessor<Integer> TARGET_ID;
    private final Tentacle tongue = new Tentacle(this, 1);

    public PalmerThing(EntityType<? extends PalmerThing> entityType, Level world) {
        super(entityType, world);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    public Tentacle getTongue(){
        return this.tongue;
    }

    @Override
    public float tentacleOriginOffset() {
        return this.getEyeHeight();
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new AbsorbGoal(this, STANDARD, Config.DIFFICULTY_CONFIG.palmerThingMergeChance.get()));
        this.goalSelector.addGoal(3, new LeapAttackGoal(this, 1.0D, false, 120, 2.0D, 0.2D, 10.0D));
        this.goalSelector.addGoal(4, new MoveThroughVillageGoal(this, 1.0, false, 4, () -> true));
        this.goalSelector.addGoal(5, new DirectedWanderGoal(this, 1.0D));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_ID, 0);
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.SPLIT_FACE.get());
    }

    public void setTargetId(int id){
        this.entityData.set(TARGET_ID, id);
    }

    public int getTargetId(){
        return this.entityData.get(TARGET_ID);
    }

    public boolean targetGrabbed(){
        if (this.getTargetId() != 0){
            Entity target = this.level().getEntity(this.getTargetId());
            if (target != null){
                if (EntityUtilities.isThing(target)){
                    this.setTargetId(0);
                    return false;
                }
                return target.distanceToSqr(this) < 25;
            }
        }
        return false;
    }

    @Override
    public Optional<CorpseBlock.CorpseType> getSuitableCorpse() {
        return Optional.of(CorpseBlock.CorpseType.HUMAN_1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()){
            this.getTongue().tick(this.targetGrabbed() ? (LivingEntity) this.level().getEntity(this.getTargetId()) : null);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 20 == 0 && this.getTarget() != null){
            this.setTargetId(this.getTarget().getId());
            if (this.targetGrabbed()){
                this.getTarget().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1), this);
            }
        }
    }

    public static AttributeSupplier.Builder createPalmerThingAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.325D).add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(FAWAnimations.defaultThing(this));
        controllerRegistrar.add(new AnimationController<>(this, "headController", 10, this::predicateHead));
    }

    private <E extends GeoEntity> PlayState predicateHead(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (this.targetGrabbed()){
            event.getController().setAnimation(FAWAnimations.OPEN_MOUTH);
        }
        else{
            event.getController().setAnimation(FAWAnimations.HEAD_IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.ELITE;
    }

    @Override
    public boolean cannotMerge() {
        return this.getVariantID() == VariantID.GORE;
    }

    static {
        TARGET_ID = SynchedEntityData.defineId(PalmerThing.class, EntityDataSerializers.INT);
    }
}
