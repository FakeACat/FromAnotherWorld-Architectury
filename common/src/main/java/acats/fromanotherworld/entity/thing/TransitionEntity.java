package acats.fromanotherworld.entity.thing;

import acats.fromanotherworld.constants.Variants;
import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.thing.resultant.BeastEntity;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.registry.SoundRegistry;
import acats.fromanotherworld.tags.DamageTypeTags;
import acats.fromanotherworld.tags.EntityTags;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static acats.fromanotherworld.constants.Variants.*;
import static acats.fromanotherworld.tags.EntityTags.*;

public class TransitionEntity extends LivingEntity implements MaybeThing {
    private static final EntityDataAccessor<CompoundTag> FAKE_ENTITY_NBT;
    private static final EntityDataAccessor<Float> WIDTH;
    private static final EntityDataAccessor<Float> HEIGHT;
    public TransitionEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        this.fixupDimensions();
        this.fakeEntity = null;
    }

    private LivingEntity fakeEntity;

    public static AttributeSupplier.Builder createTransitionAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D);
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
            transitionEntity.setFakeEntityNbt(fakeEntityNbt);
            transitionEntity.absMoveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
            transitionEntity.setSize(entity.getBbWidth(), entity.getBbHeight());
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
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        boolean vul1 = EntityUtilities.isVulnerable(this);
        boolean vul2 = source.is(DamageTypeTags.ALWAYS_HURTS_THINGS);
        return (vul1 || vul2) ? super.getDamageAfterMagicAbsorb(source, amount) : Math.min(super.getDamageAfterMagicAbsorb(source, amount), 1.0F);
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
            }
        }
        return this.fakeEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.fakeEntity != null){
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
        if (this.level().getDifficulty() == Difficulty.PEACEFUL){
            this.discard();
            return;
        }
        if (this.tickCount > 100){
            this.becomeResultant();
        }
    }

    private void clientTick(){
        for (int i = 0; i < (this.tickCount * this.tickCount) / 200; i++){
            this.level().addParticle(ParticleRegistry.THING_GORE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0, 0, 0);
        }
    }

    private float smallRandom(){
        return (this.getRandom().nextFloat() - 0.5F) * 12.0F;
    }

    private void becomeResultant(){
        ThingEntity thing = null;
        LivingEntity entity = this.getFakeEntity();
        EntityType<?> type = entity.getType();
        if (type.is(HUMANOIDS)){
            switch (chooseStrength()) {
                case 0 -> {
                    thing = EntityRegistry.CRAWLER.get().create(this.level());
                    spawnCrawlers(4);

                }
                case 1 -> thing = EntityRegistry.JULIETTE_THING.get().create(this.level());
                case 2 -> thing = EntityRegistry.PALMER_THING.get().create(this.level());
            }
            if (thing != null){
                if (type.is(VILLAGERS))
                    thing.setVictimType(VILLAGER);
                else if (type.is(ILLAGERS))
                    thing.setVictimType(ILLAGER);
            }
        }
        else if (type.is(QUADRUPEDS) || type.is(LARGE_QUADRUPEDS)){
            switch (chooseStrength()) {
                case 0 -> {
                    thing = EntityRegistry.DOGBEAST_SPITTER.get().create(this.level());
                    spawnCrawlers(3);
                }
                case 1 -> thing = EntityRegistry.DOGBEAST.get().create(this.level());
                case 2 -> thing = EntityRegistry.IMPALER.get().create(this.level());
            }
            if (thing != null){
                if (type.is(COWS))
                    thing.setVictimType(COW);
                else if (type.is(EntityTags.SHEEP))
                    thing.setVictimType(Variants.SHEEP);
                else if (type.is(PIGS))
                    thing.setVictimType(PIG);
                else if (type.is(HORSES))
                    thing.setVictimType(HORSE);
                else if (type.is(LLAMAS))
                    thing.setVictimType(LLAMA);
            }
        }
        else if (type.is(VERY_LARGE_QUADRUPEDS)){
            thing = EntityRegistry.BEAST.get().create(entity.level());
            if (thing != null){
                ((BeastEntity) thing).setTier(0, true);
            }
        }
        else{
            spawnCrawlers(Mth.ceil(vol() * 4.0F));
        }
        if (thing != null){
            thing.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            thing.initializeFrom(this);
            this.level().addFreshEntity(thing);
        }
        EntityUtilities.angerNearbyThings(2, this, null);
        this.discard();
    }

    private void spawnCrawlers(int crawlers){
        for (int i = 0; i < crawlers; i++){
            BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(this.level());
            if (bloodCrawlerEntity != null) {
                bloodCrawlerEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                bloodCrawlerEntity.initializeFrom(this);
                this.level().addFreshEntity(bloodCrawlerEntity);
            }
        }
    }

    private int chooseStrength(){
        if (this.getRandom().nextInt(10) == 0)
            return 2;
        return this.getRandom().nextInt(2);
    }

    private float vol(){
        LivingEntity entity = this.getFakeEntity();
        return entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight();
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

    static {
        FAKE_ENTITY_NBT = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.COMPOUND_TAG);
        WIDTH = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.FLOAT);
        HEIGHT = SynchedEntityData.defineId(TransitionEntity.class, EntityDataSerializers.FLOAT);
    }

    @Override
    public boolean isThing() {
        return true;
    }
}
