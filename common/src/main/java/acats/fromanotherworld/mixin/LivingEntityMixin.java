package acats.fromanotherworld.mixin;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.General;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.resultant.*;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static acats.fromanotherworld.tags.EntityTags.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DisguisedThing {
    private static final TrackedData<Float> SUPERCELL_CONCENTRATION;
    private static final TrackedData<Integer> REVEALED;
    private static final TrackedData<Integer> REVEALED_MAX;
    private static final int REVEAL_COOLDOWN = 12000;
    private int reveal_timer;
    private boolean assimilated;
    private boolean sleeper;
    @Shadow public abstract void kill();
    @Shadow public abstract void heal(float amount);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("IsAssimilated", this.assimilated);
        nbt.putBoolean("IsSleeper", this.sleeper);
        nbt.putFloat("SupercellConcentration", this.getSupercellConcentration());
        nbt.putInt("RevealTimer", this.reveal_timer);
        nbt.putInt("RevealMaximum", this.getRevealMaximum());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        if (nbt.contains("IsAssimilated")){
            this.assimilated = nbt.getBoolean("IsAssimilated");
        }
        if (nbt.contains("IsSleeper")){
            this.sleeper = nbt.getBoolean("IsSleeper");
        }
        if (nbt.contains("SupercellConcentration")){
            this.setSupercellConcentration(nbt.getFloat("SupercellConcentration"));
        }
        if (nbt.contains("RevealTimer")){
            this.reveal_timer = nbt.getInt("RevealTimer");
        }
        if (nbt.contains("RevealMaximum")){
            this.setRevealedMax(nbt.getInt("RevealMaximum"));
        }
    }

    @Inject(at = @At("HEAD"), method = "initDataTracker")
    private void simInitDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(SUPERCELL_CONCENTRATION, 0.0F);
        this.dataTracker.startTracking(REVEALED, 0);
        this.dataTracker.startTracking(REVEALED_MAX, 0);
    }
    @Override
    public void setSupercellConcentration(float i){
        this.dataTracker.set(SUPERCELL_CONCENTRATION, i);
    }

    @Override
    public float getSupercellConcentration(){
        return this.dataTracker.get(SUPERCELL_CONCENTRATION);
    }

    private void setRevealed(int t){
        this.dataTracker.set(REVEALED, t);
    }

    private int getRevealed(){
        return this.dataTracker.get(REVEALED);
    }

    private void setRevealedMax(int t){
        this.dataTracker.set(REVEALED_MAX, t);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void simTick(CallbackInfo ci){
        if (this.getSupercellConcentration() > 0){
            if (this.world.getDifficulty() == Difficulty.PEACEFUL){
                this.discard();
                return;
            }
            this.setSupercellConcentration(this.getSupercellConcentration() * 1.005F);
            if ((this.assimilated || this.tooSmallToDisguise()) && this.getSupercellConcentration() >= 50){
                this.becomeResultant();
            }
            else if (this.getSupercellConcentration() >= 100){
                this.setAssimilated();
                this.setSupercellConcentration(0);
            }
            if (this.getSupercellConcentration() >= 1.0F){
                if (!this.world.isClient() && !FromAnotherWorld.isVulnerable((LivingEntity) (Entity) this)){
                    this.heal(1.0F);
                }
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
            }
        }
        else if (this.assimilated){
            if (this.world.getDifficulty() == Difficulty.PEACEFUL){
                this.discard();
                return;
            }
            if (!this.sleeper){
                if (this.reveal_timer <= REVEAL_COOLDOWN){
                    this.reveal_timer++;
                }
                if (this.reveal_timer > REVEAL_COOLDOWN && random.nextInt(60) == 0){
                    this.tryReveal();
                }
                if (random.nextInt(9000) == 0){
                    this.tryBecomeResultant();
                }
                this.setRevealed(Math.max(this.getRevealed() - 1, 0));
            }
        }
    }

    private void setRareAbilities(int chanceDenominator){
        this.sleeper = random.nextInt(chanceDenominator) == 0;
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    private void conversionEffects(CallbackInfo ci){
        if (this.world.isClient){
            if (this.getSupercellConcentration() >= 1.0F) {
                for(int i = 0; i < this.getSupercellConcentration() / 10; ++i) {
                    this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);
                }
            }
        }
    }

    private void tryReveal(){
        int entityCheckDist = 12;
        int playerCheckDist = 32;
        PlayerEntity p = world.getClosestPlayer(this, playerCheckDist);
        if ((p == null || p.isSpectator() || p.isCreative()) && !this.world.isClient()){
            List<LivingEntity> nearbyEntities = world.getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - entityCheckDist, this.getY() - entityCheckDist, this.getZ() - entityCheckDist, this.getX() + entityCheckDist, this.getY() + entityCheckDist, this.getZ() + entityCheckDist));
            int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
            int things = FromAnotherWorld.numThingsInList(nearbyEntities);
            if (assimilables > 0 && assimilables < things * 3){
                this.reveal();
                this.reveal_timer = 0;
            }
        }
    }

    private void tryBecomeResultant(){
        int entityCheckDist = 16;
        List<LivingEntity> nearbyEntities = world.getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - entityCheckDist, this.getY() - entityCheckDist, this.getZ() - entityCheckDist, this.getX() + entityCheckDist, this.getY() + entityCheckDist, this.getZ() + entityCheckDist));
        int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
        int things = FromAnotherWorld.numThingsInList(nearbyEntities);
        if (things > 5 && assimilables <= 1 && !world.isClient()){
            this.setSupercellConcentration(this.getSupercellConcentration() + 5);
        }
    }

    private boolean tooSmallToDisguise(){
        return this.vol() < 0.125F;
    }

    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void canSimTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (this.assimilated && FromAnotherWorld.isThing(target)){
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "pushAway")
    private void assimilateOnPush(Entity entity, CallbackInfo ci){
        if (this.assimilated && random.nextInt(6000) == 0){
            FromAnotherWorld.assimilate(entity, 0.01F);
        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onSimDeath(DamageSource source, CallbackInfo ci){
        if (this.assimilated && !world.isClient()){
            FromAnotherWorld.angerNearbyThings(2, (LivingEntity) (Entity) this, source.getAttacker() instanceof LivingEntity e ? e : null);
            this.becomeResultant();
        }
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void damageSleeperReveal(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if (this.sleeper && source.getAttacker() instanceof PlayerEntity player && !world.isClient()){
            FromAnotherWorld.angerNearbyThings(1, (LivingEntity) (Entity) this, player);
            for (int i = 0; i < 20; i++){
                AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, this.getX(), this.getY(), this.getZ());
                assimilationLiquid.setVelocity(new Vec3d(random.nextDouble() - 0.5f, random.nextDouble(), random.nextDouble() - 0.5f));
                world.spawnEntity(assimilationLiquid);
            }
            this.becomeResultant();
        }
    }

    private void reveal(){
        this.setTimeUntilFinishedRevealing(400);
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 6, false, false));
        ChestSpitterEntity chestSpitterEntity = EntityRegistry.CHEST_SPITTER.get().create(this.world);
        if (chestSpitterEntity != null){
            chestSpitterEntity.setPosition(this.getPos());
            chestSpitterEntity.host = this;
            this.world.spawnEntity(chestSpitterEntity);
        }
    }

    private void becomeResultant(){
        if (this.world.isClient()) {
            return;
        }
        AbstractThingEntity entity = null;
        if (this.getType().isIn(HUMANOIDS)){
            switch (this.chooseStrength()) {
                case 0 -> {
                    entity = EntityRegistry.CRAWLER.get().create(this.world);
                    this.spawnCrawlers(4, this.getPos());

                }
                case 1 -> entity = EntityRegistry.JULIETTE_THING.get().create(this.world);
                case 2 -> entity = EntityRegistry.PALMER_THING.get().create(this.world);
            }
        }
        else if (this.getType().isIn(QUADRUPEDS)){
            if (this.vol() > 2.25F){
                entity = EntityRegistry.BEAST.get().create(this.world);
                if (entity != null){
                    ((BeastEntity) entity).setTier(0, true);
                }
            }
            else{
                switch (this.chooseStrength()) {
                    case 0 -> {
                        entity = EntityRegistry.DOGBEAST_SPITTER.get().create(this.world);
                        this.spawnCrawlers(3, this.getPos());
                    }
                    case 1 -> entity = EntityRegistry.DOGBEAST.get().create(this.world);
                    case 2 -> entity = EntityRegistry.IMPALER.get().create(this.world);
                }
            }
        }
        else{
            this.spawnCrawlers(MathHelper.ceil(this.vol() * 4.0F), this.getPos());
        }
        if (entity != null){
            entity.setVictimType(EntityType.getId(this.getType()).toString());
            entity.setPosition(this.getPos());
            this.world.spawnEntity(entity);
        }
        this.discard();
    }

    float vol(){
        return this.getWidth() * this.getWidth() * this.getHeight();
    }

    private void spawnCrawlers(int crawlers, Vec3d pos){
        for (int i = 0; i < crawlers; i++){
            BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(this.world);
            if (bloodCrawlerEntity != null) {
                bloodCrawlerEntity.setPosition(pos);
                this.world.spawnEntity(bloodCrawlerEntity);
            }
        }
    }

    private int chooseStrength(){
        if (random.nextInt(10) == 0)
            return 2;
        return random.nextInt(2);
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
        return this.dataTracker.get(REVEALED_MAX);
    }

    @Override
    public boolean isAssimilated() {
        return this.assimilated;
    }

    @Override
    public void setAssimilated() {
        this.assimilated = true;
        this.setRareAbilities(General.specialBehaviourRarity);
    }

    static {
        SUPERCELL_CONCENTRATION = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
        REVEALED = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
        REVEALED_MAX = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
