package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.interfaces.BurstAttackThing;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.entity.goal.ThingProjectileBurstGoal;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ImpalerEntity extends AbsorberThingEntity implements BurstAttackThing {

    private static final EntityDataAccessor<Boolean> BACK_NEEDLES;
    private int backNeedlesRegrow = 0;
    private static final EntityDataAccessor<Boolean> MOUTH_NEEDLES;
    private int mouthNeedlesRegrow = 0;

    public ImpalerEntity(EntityType<? extends ImpalerEntity> entityType, Level world) {
        super(entityType, world);
    }

    public void setBackNeedles(boolean b){
        this.entityData.set(BACK_NEEDLES, b);
    }

    public boolean hasBackNeedles() {
        return this.entityData.get(BACK_NEEDLES);
    }

    public void setMouthNeedles(boolean b){
        this.entityData.set(MOUTH_NEEDLES, b);
    }

    public boolean hasMouthNeedles() {
        return this.entityData.get(MOUTH_NEEDLES);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new ThingProjectileBurstGoal(this, 16.0F, 30));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(2, new ThingAttackGoal(this, 1.0D, false));
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

    public static AttributeSupplier.Builder createImpalerAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        if (this.hasBackNeedles()){
            for (int i = 0; i < 8; i++){
                NeedleEntity needleEntity = new NeedleEntity(this.level(), this.getX(), this.getY() + 0.8F, this.getZ(), this);
                needleEntity.setDeltaMovement(new Vec3(random.nextDouble() - 0.5D, random.nextDouble(), random.nextDouble() - 0.5D).scale(0.75D));
                this.level().addFreshEntity(needleEntity);
            }
            this.setBackNeedles(false);
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()){
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
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.BEAST.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BACK_NEEDLES, true);
        this.entityData.define(MOUTH_NEEDLES, true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("BackNeedles", this.hasBackNeedles());
        nbt.putInt("BackNeedlesRegrow", this.backNeedlesRegrow);
        nbt.putBoolean("MouthNeedles", this.hasMouthNeedles());
        nbt.putInt("MouthNeedlesRegrow", this.mouthNeedlesRegrow);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setBackNeedles(nbt.getBoolean("BackNeedles"));
        this.backNeedlesRegrow = nbt.getInt("BackNeedlesRegrow");
        this.setMouthNeedles(nbt.getBoolean("MouthNeedles"));
        this.mouthNeedlesRegrow = nbt.getInt("MouthNeedlesRegrow");
    }

    static {
        BACK_NEEDLES = SynchedEntityData.defineId(ImpalerEntity.class, EntityDataSerializers.BOOLEAN);
        MOUTH_NEEDLES = SynchedEntityData.defineId(ImpalerEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public void shootBurst(LivingEntity target) {
        this.setMouthNeedles(false);
        if (!this.level().isClientSide()){
            for (int i = 0; i < 15; i++){
                NeedleEntity needle = new NeedleEntity(this.level(), this);
                needle.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(needle.position()).normalize().add(new Vec3(random.nextInt(40) - 20, random.nextInt(40) - 20, random.nextInt(40) - 20).scale(0.01f)).scale(3.0D));
                this.level().addFreshEntity(needle);
            }
        }
    }

    @Override
    public boolean canShootBurst() {
        return this.hasMouthNeedles();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(FAWAnimations.defaultThingNoChase(this));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD_STRONG;
    }
}
