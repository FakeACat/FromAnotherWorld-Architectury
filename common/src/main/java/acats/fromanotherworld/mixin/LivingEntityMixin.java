package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PossibleDisguisedThing, MaybeThing {
    private SynchedEntityData dataTracker(){
        return ((LivingEntity) (Object) this).getEntityData();
    }
    private static final EntityDataAccessor<Float> SUPERCELL_CONCENTRATION;
    private static final EntityDataAccessor<Integer> REVEALED;
    private static final EntityDataAccessor<Integer> REVEALED_MAX;
    private int revealTimer;
    private boolean assimilated;
    private boolean sleeper;

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci){
        nbt.putBoolean("IsAssimilated", this.isAssimilated());
        nbt.putBoolean("IsSleeper", this.isSleeper());
        nbt.putFloat("SupercellConcentration", this.getSupercellConcentration());
        nbt.putInt("RevealTimer", this.getRevealTimer());
        nbt.putInt("RevealMaximum", this.getRevealMaximum());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci){
        if (nbt.contains("IsAssimilated")){
            this.setAssimilated(nbt.getBoolean("IsAssimilated"));
        }
        if (nbt.contains("IsSleeper")){
            this.setSleeper(nbt.getBoolean("IsSleeper"));
        }
        if (nbt.contains("SupercellConcentration")){
            this.setSupercellConcentration(nbt.getFloat("SupercellConcentration"));
        }
        if (nbt.contains("RevealTimer")){
            this.setRevealTimer(nbt.getInt("RevealTimer"));
        }
        if (nbt.contains("RevealMaximum")){
            this.setRevealedMax(nbt.getInt("RevealMaximum"));
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
    public void setSupercellConcentration(float i){
        this.dataTracker().set(SUPERCELL_CONCENTRATION, i);
    }

    @Override
    public float getSupercellConcentration(){
        return this.dataTracker().get(SUPERCELL_CONCENTRATION);
    }

    @Override
    public int getTimeUntilFinishedRevealing() {
        return this.getRevealed();
    }

    @Override
    public void setTimeUntilFinishedRevealing(int t) {
        this.setRevealed(t);
        this.setRevealedMax(t / 2);
    }

    @Override
    public int getRevealMaximum() {
        return this.dataTracker().get(REVEALED_MAX);
    }

    @Override
    public boolean isAssimilated() {
        return this.assimilated;
    }

    @Override
    public void setAssimilated(boolean assimilated) {
        this.assimilated = assimilated;
    }

    @Override
    public boolean isSleeper() {
        return this.sleeper;
    }

    @Override
    public void setSleeper(boolean sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public int getRevealTimer() {
        return this.revealTimer;
    }

    @Override
    public void setRevealTimer(int revealTimer) {
        this.revealTimer = revealTimer;
    }

    @Override
    public int getRevealed(){
        return this.dataTracker().get(REVEALED);
    }

    @Override
    public void setRevealed(int t){
        this.dataTracker().set(REVEALED, t);
    }

    @Override
    public void setRevealedMax(int t){
        this.dataTracker().set(REVEALED_MAX, t);
    }

    @Override
    public boolean isThing() {
        return this.isAssimilated();
    }

    static{
        SUPERCELL_CONCENTRATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
        REVEALED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        REVEALED_MAX = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    }
}
