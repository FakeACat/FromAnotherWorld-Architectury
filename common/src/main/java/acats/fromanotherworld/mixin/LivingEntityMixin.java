package acats.fromanotherworld.mixin;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PossibleDisguisedThing {
    private DataTracker dataTracker(){
        return ((LivingEntity) (Object) this).getDataTracker();
    }
    private static final TrackedData<Float> SUPERCELL_CONCENTRATION;
    private static final TrackedData<Integer> REVEALED;
    private static final TrackedData<Integer> REVEALED_MAX;
    private int revealTimer;
    private boolean assimilated;
    private boolean sleeper;

    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("IsAssimilated", this.isAssimilated());
        nbt.putBoolean("IsSleeper", this.isSleeper());
        nbt.putFloat("SupercellConcentration", this.getSupercellConcentration());
        nbt.putInt("RevealTimer", this.getRevealTimer());
        nbt.putInt("RevealMaximum", this.getRevealMaximum());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
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

    @Inject(at = @At("HEAD"), method = "initDataTracker")
    private void initDataTracker(CallbackInfo ci){
        this.dataTracker().startTracking(SUPERCELL_CONCENTRATION, 0.0F);
        this.dataTracker().startTracking(REVEALED, 0);
        this.dataTracker().startTracking(REVEALED_MAX, 0);
    }


    @Inject(at = @At("HEAD"), method = "pushAway")
    private void pushAway(Entity entity, CallbackInfo ci){
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

    static{
        SUPERCELL_CONCENTRATION = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);
        REVEALED = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        REVEALED_MAX = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
