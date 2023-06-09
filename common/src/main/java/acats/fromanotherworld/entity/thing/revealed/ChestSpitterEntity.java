package acats.fromanotherworld.entity.thing.revealed;

import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.thing.ThingEntity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class ChestSpitterEntity extends ThingEntity {
    private static final int REVEAL_TIME = 100;
    private static final int ATTACK_TIME = 100;
    public Entity host;

    public ChestSpitterEntity(EntityType<? extends ChestSpitterEntity> entityType, Level world) {
        super(entityType, world, false);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new LookAtTargetGoal(this));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.tickCount < REVEAL_TIME){
            event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("animation.chest_spitter.emerge"));
        }
        else if (this.tickCount < REVEAL_TIME + ATTACK_TIME){
            event.getController().setTransitionLength(4);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.chest_spitter.spit"));
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("animation.chest_spitter.retract"));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createChestSpitterAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    public boolean canThingFreeze() {
        return false;
    }

    @Override
    public boolean save(CompoundTag nbt) {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void push(Entity entity) {
    }
    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > REVEAL_TIME + 10 && this.tickCount < REVEAL_TIME + ATTACK_TIME && !this.level().isClientSide() && this.getTarget() != null){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.level(), this);
            assimilationLiquid.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 2.5F, 10.0F);
            double d = 0.5D;
            assimilationLiquid.setPosRaw(assimilationLiquid.getX() + assimilationLiquid.getDeltaMovement().x * d, assimilationLiquid.getY() + assimilationLiquid.getDeltaMovement().y * d, assimilationLiquid.getZ() + assimilationLiquid.getDeltaMovement().z * d);
            this.level().addFreshEntity(assimilationLiquid);
        }

        if (this.tickCount > 2 * REVEAL_TIME + ATTACK_TIME - 20){
            this.discard();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.host == null || !this.host.isAlive()){
            this.kill();
        }
        else{
            Vec3 pos = this.host.position().add(0, (this.host.getBbHeight() * 0.55) - (this.getBbHeight() / 2), 0);
            this.setPos(pos);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source != this.level().damageSources().inWall() && super.hurt(source, amount);
    }

    @Override
    public boolean isDistinctThing() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.REVEALED;
    }

    static class LookAtTargetGoal extends Goal {
        private final ChestSpitterEntity spitter;

        public LookAtTargetGoal(ChestSpitterEntity spitter) {
            this.spitter = spitter;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.spitter.getTarget() != null) {
                LivingEntity vec3d = this.spitter.getTarget();
                double d = 64.0D;
                if (vec3d.distanceToSqr(this.spitter) < d*d) {
                    double e = vec3d.getX() - this.spitter.getX();
                    double f = vec3d.getZ() - this.spitter.getZ();
                    this.spitter.setYRot(-((float)Mth.atan2(e, f)) * 57.295776F);
                    this.spitter.yBodyRot = this.spitter.getYRot();
                }
            }
        }
    }
}
