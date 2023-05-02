package acats.fromanotherworld.entity.revealed;

import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ChestSpitterEntity extends AbstractThingEntity {

    private final AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    private static final int REVEAL_TIME = 100;
    private static final int ATTACK_TIME = 100;
    public Entity host;

    public ChestSpitterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world, false);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new LookAtTargetGoal(this));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.age < REVEAL_TIME){
            event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("animation.chest_spitter.emerge"));
        }
        else if (this.age < REVEAL_TIME + ATTACK_TIME){
            event.getController().setTransitionLength(4);
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.chest_spitter.spit"));
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("animation.chest_spitter.retract"));
        }
        return PlayState.CONTINUE;
    }

    public static DefaultAttributeContainer.Builder createChestSpitterAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D);
    }

    @Override
    public boolean canThingFreeze() {
        return false;
    }

    @Override
    public boolean saveNbt(NbtCompound nbt) {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }
    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > REVEAL_TIME + 10 && this.age < REVEAL_TIME + ATTACK_TIME && !this.world.isClient() && this.getTarget() != null){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, this);
            assimilationLiquid.setVelocity(this, this.getPitch(), this.getYaw(), 0.0F, 2.5F, 10.0F);
            double d = 0.5D;
            assimilationLiquid.setPos(assimilationLiquid.getX() + assimilationLiquid.getVelocity().x * d, assimilationLiquid.getY() + assimilationLiquid.getVelocity().y * d, assimilationLiquid.getZ() + assimilationLiquid.getVelocity().z * d);
            world.spawnEntity(assimilationLiquid);
        }

        if (this.age > 2 * REVEAL_TIME + ATTACK_TIME - 20){
            this.discard();
        }
    }

    @Override
    public boolean canMerge() {
        return false;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.host == null || !this.host.isAlive()){
            this.kill();
        }
        else{
            Vec3d pos = this.host.getPos().add(0, (this.host.getHeight() * 0.55) - (this.getHeight() / 2), 0);
            this.setPosition(pos);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return source != this.world.getDamageSources().inWall() && super.damage(source, amount);
    }

    @Override
    public boolean shouldBeCounted() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public Strength getFormStrength() {
        return Strength.REVEALED;
    }

    static class LookAtTargetGoal extends Goal {
        private final ChestSpitterEntity spitter;

        public LookAtTargetGoal(ChestSpitterEntity spitter) {
            this.spitter = spitter;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.spitter.getTarget() != null) {
                LivingEntity vec3d = this.spitter.getTarget();
                double d = 64.0D;
                if (vec3d.squaredDistanceTo(this.spitter) < d*d) {
                    double e = vec3d.getX() - this.spitter.getX();
                    double f = vec3d.getZ() - this.spitter.getZ();
                    this.spitter.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.spitter.bodyYaw = this.spitter.getYaw();
                }
            }
        }
    }
}
