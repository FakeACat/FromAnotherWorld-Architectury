package acats.fromanotherworld.entity.thing;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.constants.Variants;
import acats.fromanotherworld.entity.thing.resultant.BeastEntity;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.SoundRegistry;
import acats.fromanotherworld.tags.DamageTypeTags;
import acats.fromanotherworld.tags.EntityTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static acats.fromanotherworld.constants.Variants.*;
import static acats.fromanotherworld.tags.EntityTags.*;

public class TransitionEntity extends LivingEntity {
    private static final TrackedData<NbtCompound> FAKE_ENTITY_NBT;
    public TransitionEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.fakeEntity = null;
    }

    private LivingEntity fakeEntity;

    public static DefaultAttributeContainer.Builder createTransitionAttributes(){
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FAKE_ENTITY_NBT, new NbtCompound());
    }

    public static void createFrom(LivingEntity entity){
        if (entity.getWorld().isClient())
            return;
        TransitionEntity transitionEntity = EntityRegistry.TRANSITION.get().create(entity.getWorld());
        if (transitionEntity != null){
            NbtCompound fakeEntityNbt = new NbtCompound();
            entity.saveSelfNbt(fakeEntityNbt);
            fakeEntityNbt.putShort("HurtTime", (short) 0);
            fakeEntityNbt.putInt("HurtByTimestamp", 0);
            fakeEntityNbt.putShort("DeathTime", (short) 0);
            transitionEntity.setFakeEntityNbt(fakeEntityNbt);
            transitionEntity.updatePositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
            entity.getWorld().spawnEntity(transitionEntity);
        }
    }

    @Override
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        boolean vul1 = FromAnotherWorld.isVulnerable(this);
        boolean vul2 = source.isIn(DamageTypeTags.ALWAYS_HURTS_THINGS);
        return (vul1 || vul2) ? super.modifyAppliedDamage(source, amount) : Math.min(super.modifyAppliedDamage(source, amount), 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.GENERAL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.GENERAL_DEATH.get();
    }

    private NbtCompound getFakeEntityNbt(){
        return this.dataTracker.get(FAKE_ENTITY_NBT);
    }
    private void setFakeEntityNbt(NbtCompound nbt){
        this.dataTracker.set(FAKE_ENTITY_NBT, nbt);
    }

    public @NotNull LivingEntity getFakeEntity(){
        if (this.fakeEntity == null){
            EntityType.getEntityFromNbt(this.getFakeEntityNbt(), this.getWorld()).ifPresent(entity -> this.fakeEntity = (LivingEntity) entity);
            if (this.fakeEntity == null){
                this.fakeEntity = new PigEntity(EntityType.PIG, this.getWorld());
            }
        }
        return this.fakeEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.fakeEntity != null){
            this.fakeEntity.prevYaw = this.fakeEntity.getYaw();
            this.fakeEntity.prevHeadYaw = this.fakeEntity.getHeadYaw();
            this.fakeEntity.prevBodyYaw = this.fakeEntity.getBodyYaw();
            this.fakeEntity.prevPitch = this.fakeEntity.getPitch();
            this.fakeEntity.setYaw(this.getYaw());
            this.fakeEntity.setPitch(this.getPitch());
            this.fakeEntity.setHeadYaw(this.getHeadYaw());
            this.fakeEntity.setBodyYaw(this.getBodyYaw());
        }
        if (this.getWorld().isClient()) {
            return;
        }
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL){
            this.discard();
            return;
        }
        if (this.age > 60){
            this.becomeResultant();
        }
    }

    private void becomeResultant(){
        ThingEntity thing = null;
        LivingEntity entity = this.getFakeEntity();
        EntityType<?> type = entity.getType();
        if (type.isIn(HUMANOIDS)){
            switch (chooseStrength()) {
                case 0 -> {
                    thing = EntityRegistry.CRAWLER.get().create(this.getWorld());
                    spawnCrawlers(4);

                }
                case 1 -> thing = EntityRegistry.JULIETTE_THING.get().create(this.getWorld());
                case 2 -> thing = EntityRegistry.PALMER_THING.get().create(this.getWorld());
            }
            if (thing != null){
                if (type.isIn(VILLAGERS))
                    thing.setVictimType(VILLAGER);
                else if (type.isIn(ILLAGERS))
                    thing.setVictimType(ILLAGER);
            }
        }
        else if (type.isIn(QUADRUPEDS) || type.isIn(LARGE_QUADRUPEDS)){
            switch (chooseStrength()) {
                case 0 -> {
                    thing = EntityRegistry.DOGBEAST_SPITTER.get().create(this.getWorld());
                    spawnCrawlers(3);
                }
                case 1 -> thing = EntityRegistry.DOGBEAST.get().create(this.getWorld());
                case 2 -> thing = EntityRegistry.IMPALER.get().create(this.getWorld());
            }
            if (thing != null){
                if (type.isIn(COWS))
                    thing.setVictimType(COW);
                else if (type.isIn(EntityTags.SHEEP))
                    thing.setVictimType(Variants.SHEEP);
                else if (type.isIn(PIGS))
                    thing.setVictimType(PIG);
                else if (type.isIn(HORSES))
                    thing.setVictimType(HORSE);
                else if (type.isIn(LLAMAS))
                    thing.setVictimType(LLAMA);
            }
        }
        else if (type.isIn(VERY_LARGE_QUADRUPEDS)){
            thing = EntityRegistry.BEAST.get().create(entity.world);
            if (thing != null){
                ((BeastEntity) thing).setTier(0, true);
            }
        }
        else{
            spawnCrawlers(MathHelper.ceil(vol() * 4.0F));
        }
        if (thing != null){
            thing.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
            thing.initializeFrom(this);
            this.world.spawnEntity(thing);
        }
        this.discard();
    }

    private void spawnCrawlers(int crawlers){
        for (int i = 0; i < crawlers; i++){
            BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(this.getWorld());
            if (bloodCrawlerEntity != null) {
                bloodCrawlerEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
                bloodCrawlerEntity.initializeFrom(this);
                this.getWorld().spawnEntity(bloodCrawlerEntity);
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
        return entity.getWidth() * entity.getWidth() * entity.getHeight();
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return DefaultedList.ofSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.put("FakeEntity", this.getFakeEntityNbt());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("FakeEntity")){
            this.setFakeEntityNbt(nbt.getCompound("FakeEntity"));
        }
    }

    static {
        FAKE_ENTITY_NBT = DataTracker.registerData(TransitionEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    }
}
