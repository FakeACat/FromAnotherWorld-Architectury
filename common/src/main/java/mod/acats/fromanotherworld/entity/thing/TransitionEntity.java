package mod.acats.fromanotherworld.entity.thing;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.interfaces.MaybeThing;
import mod.acats.fromanotherworld.entity.render.thing.growths.TentacleMass;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.registry.SoundRegistry;
import mod.acats.fromanotherworld.tags.DamageTypeTags;
import mod.acats.fromanotherworld.transformation.FormSelector;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TransitionEntity extends LivingEntity implements MaybeThing {
    private static final EntityDataAccessor<CompoundTag> FAKE_ENTITY_NBT;
    private static final EntityDataAccessor<Float> WIDTH;
    private static final EntityDataAccessor<Float> HEIGHT;
    private static final int MAX_AGE = 100;
    private static final int SPAWN_MOB_AGE = 90;
    public final TentacleMass tentacleMass;
    public TransitionEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        this.fixupDimensions();
        this.fakeEntity = null;
        this.tentacleMass = new TentacleMass(world, 30, 20, 0.0F, 0.95F);
    }

    private LivingEntity fakeEntity;

    public static AttributeSupplier.Builder createTransitionAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FAKE_ENTITY_NBT, new CompoundTag());
        this.entityData.define(WIDTH, 1.0F);
        this.entityData.define(HEIGHT, 1.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (WIDTH.equals(data) || HEIGHT.equals(data)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(data);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.entityData.get(WIDTH), this.entityData.get(HEIGHT));
    }

    public static void createFrom(LivingEntity entity){
        if (entity.level().isClientSide())
            return;
        TransitionEntity transitionEntity = EntityRegistry.TRANSITION.get().create(entity.level());
        if (transitionEntity != null){
            CompoundTag fakeEntityNbt = new CompoundTag();
            entity.saveAsPassenger(fakeEntityNbt);
            fakeEntityNbt.putShort("HurtTime", (short) 0);
            fakeEntityNbt.putInt("HurtByTimestamp", 0);
            fakeEntityNbt.putShort("DeathTime", (short) 0);
            fakeEntityNbt.putUUID("UUID", UUID.randomUUID());
            transitionEntity.setFakeEntityNbt(fakeEntityNbt);
            transitionEntity.absMoveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
            transitionEntity.setSize(entity.getBbWidth(), entity.getBbHeight());
            transitionEntity.setCustomName(entity.getCustomName());
            entity.level().addFreshEntity(transitionEntity);
        }
    }

    private void setSize(float width, float height){
        this.entityData.set(WIDTH, width);
        this.entityData.set(HEIGHT, height);
        this.reapplyPosition();
        this.refreshDimensions();
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();
        this.setPos(d, e, f);
        tentacleMass.rootYOffset = this.entityData.get(HEIGHT) * 0.5F;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        boolean vul1 = EntityUtilities.isVulnerable(this);
        boolean vul2 = source.is(DamageTypeTags.ALWAYS_HURTS_THINGS);
        return (vul1 || vul2) ? super.getDamageAfterMagicAbsorb(source, amount) : super.getDamageAfterMagicAbsorb(source, amount) * Thing.ThingCategory.REVEALED.getDamageMultiplierWhenNotBurning();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.GENERAL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.GENERAL_DEATH.get();
    }

    private CompoundTag getFakeEntityNbt(){
        return this.entityData.get(FAKE_ENTITY_NBT);
    }
    private void setFakeEntityNbt(CompoundTag nbt){
        this.entityData.set(FAKE_ENTITY_NBT, nbt);
    }

    public @NotNull LivingEntity getFakeEntity(){
        if (this.fakeEntity == null){
            EntityType.create(this.getFakeEntityNbt(), this.level()).ifPresent(entity -> this.fakeEntity = (LivingEntity) entity);
            if (this.fakeEntity == null){
                this.fakeEntity = new Pig(EntityType.PIG, this.level());
                FromAnotherWorld.LOGGER.error("Transition entity has a null fake entity!");
                this.discard();
            }
        }
        return this.fakeEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.shouldRenderFakeEntity() && this.fakeEntity != null){
            this.fakeEntity.yRotO = this.fakeEntity.getYRot();
            this.fakeEntity.yHeadRotO = this.fakeEntity.getYHeadRot();
            this.fakeEntity.yBodyRotO = this.fakeEntity.getVisualRotationYInDegrees();
            this.fakeEntity.xRotO = this.fakeEntity.getXRot();
            this.fakeEntity.setYRot(this.fakeEntity.getYRot() + this.smallRandom());
            this.fakeEntity.setXRot(this.fakeEntity.getXRot() + this.smallRandom());
            this.fakeEntity.setYHeadRot(this.fakeEntity.getYHeadRot() + this.smallRandom());
            this.fakeEntity.setYBodyRot(this.fakeEntity.getVisualRotationYInDegrees() + this.smallRandom());
        }
        if (this.level().isClientSide()) {
            this.clientTick();
            return;
        }
        if (this.tickCount % 10 == 0 && !EntityUtilities.isVulnerable(this)){
            this.heal(1.0F);
        }
        if (this.level().getDifficulty() == Difficulty.PEACEFUL){
            this.discard();
            return;
        }
        if (this.tickCount == SPAWN_MOB_AGE){
            this.becomeResultant();
        }
        if (this.tickCount == MAX_AGE){
            this.discard();
        }
    }

    public boolean shouldRenderFakeEntity(){
        return this.tickCount < SPAWN_MOB_AGE;
    }

    private void clientTick(){
        tentacleMass.tick();
        if (this.tickCount < MAX_AGE * 0.2F){
            tentacleMass.scale = this.tickCount / (MAX_AGE * 0.2F);
        }
        else if (this.tickCount > MAX_AGE * 0.9F){
            tentacleMass.scale = 10.0F - this.tickCount / (MAX_AGE * 0.1F);
        }
        else{
            tentacleMass.scale = 1.0F;
        }
        tentacleMass.scale *= 0.75F * this.entityData.get(WIDTH);
        for (int i = 0; i < (this.tickCount * this.tickCount) / 400; i++){
            this.level().addParticle(ParticleRegistry.THING_GORE.get(), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0, 0, 0);
        }
    }

    private float smallRandom(){
        return (this.getRandom().nextFloat() - 0.5F) * 12.0F;
    }

    private void becomeResultant(){
        TransformationContext ctx = new TransformationContext(
                (ServerLevel) this.level(),
                this.getFakeEntity(),
                this.getFakeEntityNbt(),
                this.position(),
                this.getXRot(),
                this.getYRot(),
                this.getCustomName(),
                this.entityData.get(WIDTH),
                this.entityData.get(HEIGHT));

        FormSelector.getWeightedRandomFor(ctx).transform(ctx);
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.put("FakeEntity", this.getFakeEntityNbt());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt.contains("FakeEntity")){
            this.setFakeEntityNbt(nbt.getCompound("FakeEntity"));
        }
    }

    @Override
    public boolean shouldBeSaved() {
        return this.tickCount < SPAWN_MOB_AGE;
    }

    @Override
    public void push(Entity entity) {

    }

    @Override
    protected void doPush(Entity entity) {

    }

    static {
        FAKE_ENTITY_NBT = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.COMPOUND_TAG);
        WIDTH = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.FLOAT);
        HEIGHT = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.FLOAT);
    }

    @Override
    public boolean faw$isThing() {
        return true;
    }
}
