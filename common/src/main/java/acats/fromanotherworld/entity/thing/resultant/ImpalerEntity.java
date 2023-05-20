package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.entity.interfaces.BurstAttackThing;
import acats.fromanotherworld.entity.goal.MergeGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.goal.ThingProjectileBurstGoal;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImpalerEntity extends AbstractThingEntity implements BurstAttackThing {

    private static final TrackedData<Boolean> BACK_NEEDLES;
    private int backNeedlesRegrow = 0;
    private static final TrackedData<Boolean> MOUTH_NEEDLES;
    private int mouthNeedlesRegrow = 0;

    public ImpalerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setBackNeedles(boolean b){
        this.dataTracker.set(BACK_NEEDLES, b);
    }

    public boolean hasBackNeedles() {
        return this.dataTracker.get(BACK_NEEDLES);
    }

    public void setMouthNeedles(boolean b){
        this.dataTracker.set(MOUTH_NEEDLES, b);
    }

    public boolean hasMouthNeedles() {
        return this.dataTracker.get(MOUTH_NEEDLES);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new ThingProjectileBurstGoal(this, 16.0F, 30));
        this.goalSelector.add(1, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new MergeGoal(this, EntityRegistry.BEAST.get()));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if ((event.isMoving() || this.movingClimbing()) && !this.isThingFrozen()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.impaler.walk"));
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
        return -0.5F;
    }

    public static DefaultAttributeContainer.Builder createImpalerAttributes(){
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void applyDamage(DamageSource source, float amount) {
        if (this.hasBackNeedles()){
            for (int i = 0; i < 8; i++){
                NeedleEntity needleEntity = new NeedleEntity(world, this.getX(), this.getY() + 0.8F, this.getZ(), this);
                needleEntity.setVelocity(new Vec3d(random.nextDouble() - 0.5D, random.nextDouble(), random.nextDouble() - 0.5D).multiply(0.75D));
                world.spawnEntity(needleEntity);
            }
            this.setBackNeedles(false);
        }
        super.applyDamage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient()){
            if (!this.hasBackNeedles() && ++this.backNeedlesRegrow > 1200){
                this.setBackNeedles(true);
                this.backNeedlesRegrow = 0;
            }
            if (!this.hasMouthNeedles() && ++this.mouthNeedlesRegrow > 1200){
                this.setMouthNeedles(true);
                this.mouthNeedlesRegrow = 0;
            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BACK_NEEDLES, true);
        this.dataTracker.startTracking(MOUTH_NEEDLES, true);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("BackNeedles", this.hasBackNeedles());
        nbt.putInt("BackNeedlesRegrow", this.backNeedlesRegrow);
        nbt.putBoolean("MouthNeedles", this.hasMouthNeedles());
        nbt.putInt("MouthNeedlesRegrow", this.mouthNeedlesRegrow);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBackNeedles(nbt.getBoolean("BackNeedles"));
        this.backNeedlesRegrow = nbt.getInt("BackNeedlesRegrow");
        this.setMouthNeedles(nbt.getBoolean("MouthNeedles"));
        this.mouthNeedlesRegrow = nbt.getInt("MouthNeedlesRegrow");
    }

    static {
        BACK_NEEDLES = DataTracker.registerData(ImpalerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        MOUTH_NEEDLES = DataTracker.registerData(ImpalerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public void shootBurst(LivingEntity target) {
        this.setMouthNeedles(false);
        if (!world.isClient()){
            for (int i = 0; i < 15; i++){
                NeedleEntity needle = new NeedleEntity(this.world, this);
                needle.setVelocity(target.getPos().add(0, target.getHeight() / 2, 0).subtract(needle.getPos()).normalize().add(new Vec3d(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).multiply(0.01f)).multiply(3.0D));
                this.world.spawnEntity(needle);
            }
        }
    }

    @Override
    public boolean canShootBurst() {
        return this.hasMouthNeedles();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD_STRONG;
    }
}
