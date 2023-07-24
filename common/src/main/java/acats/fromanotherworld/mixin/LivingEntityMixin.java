package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.CoordinatedThing;
import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import acats.fromanotherworld.memory.ThingBaseOfOperations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PossibleDisguisedThing, MaybeThing, CoordinatedThing {
    @Unique
    private SynchedEntityData dataTracker(){
        return ((LivingEntity) (Object) this).getEntityData();
    }
    @Unique
    private static final EntityDataAccessor<Float> SUPERCELL_CONCENTRATION;
    @Unique
    private static final EntityDataAccessor<Integer> REVEALED;
    @Unique
    private static final EntityDataAccessor<Integer> REVEALED_MAX;
    @Unique
    private int revealTimer;
    @Unique
    private boolean assimilated;
    @Unique
    private boolean sleeper;
    @Unique
    private ThingBaseOfOperations base = null;

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci){
        nbt.putBoolean("IsAssimilated", this.faw$isAssimilated());
        nbt.putBoolean("IsSleeper", this.faw$isSleeper());
        nbt.putFloat("SupercellConcentration", this.faw$getSupercellConcentration());
        nbt.putInt("RevealTimer", this.faw$getRevealTimer());
        nbt.putInt("RevealMaximum", this.faw$getRevealMaximum());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci){
        if (nbt.contains("IsAssimilated")){
            this.faw$setAssimilated(nbt.getBoolean("IsAssimilated"));
        }
        if (nbt.contains("IsSleeper")){
            this.faw$setSleeper(nbt.getBoolean("IsSleeper"));
        }
        if (nbt.contains("SupercellConcentration")){
            this.faw$setSupercellConcentration(nbt.getFloat("SupercellConcentration"));
        }
        if (nbt.contains("RevealTimer")){
            this.faw$setRevealTimer(nbt.getInt("RevealTimer"));
        }
        if (nbt.contains("RevealMaximum")){
            this.faw$setRevealedMax(nbt.getInt("RevealMaximum"));
        }
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void defineSynchedData(CallbackInfo ci){
        this.dataTracker().define(SUPERCELL_CONCENTRATION, 0.0F);
        this.dataTracker().define(REVEALED, 0);
        this.dataTracker().define(REVEALED_MAX, 0);
    }


    @Inject(at = @At("HEAD"), method = "push")
    private void push(Entity entity, CallbackInfo ci){
        CommonLivingEntityEvents.pushAway((LivingEntity) (Object) this, entity);
    }

    @Override
    public void faw$setSupercellConcentration(float i){
        this.dataTracker().set(SUPERCELL_CONCENTRATION, i);
    }

    @Override
    public float faw$getSupercellConcentration(){
        return this.dataTracker().get(SUPERCELL_CONCENTRATION);
    }

    @Override
    public int faw$getTimeUntilFinishedRevealing() {
        return this.faw$getRevealed();
    }

    @Override
    public void faw$setTimeUntilFinishedRevealing(int t) {
        this.faw$setRevealed(t);
        this.faw$setRevealedMax(t / 2);
    }

    @Override
    public int faw$getRevealMaximum() {
        return this.dataTracker().get(REVEALED_MAX);
    }

    @Override
    public boolean faw$isAssimilated() {
        return this.assimilated;
    }

    @Override
    public void faw$setAssimilated(boolean assimilated) {
        this.assimilated = assimilated;
    }

    @Override
    public boolean faw$isSleeper() {
        return this.sleeper;
    }

    @Override
    public void faw$setSleeper(boolean sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public int faw$getRevealTimer() {
        return this.revealTimer;
    }

    @Override
    public void faw$setRevealTimer(int revealTimer) {
        this.revealTimer = revealTimer;
    }

    @Override
    public int faw$getRevealed(){
        return this.dataTracker().get(REVEALED);
    }

    @Override
    public void faw$setRevealed(int t){
        this.dataTracker().set(REVEALED, t);
    }

    @Override
    public void faw$setRevealedMax(int t){
        this.dataTracker().set(REVEALED_MAX, t);
    }

    @Override
    public boolean faw$isThing() {
        return this.faw$isAssimilated();
    }

    @Override
    public @Nullable ThingBaseOfOperations faw$getBase() {
        return this.base;
    }

    @Override
    public void faw$setBase(@Nullable ThingBaseOfOperations base) {
        this.base = base;
    }

    static{
        SUPERCELL_CONCENTRATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
        REVEALED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        REVEALED_MAX = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    }
}
