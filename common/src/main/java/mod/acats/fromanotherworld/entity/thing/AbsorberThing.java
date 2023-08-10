package mod.acats.fromanotherworld.entity.thing;

import mod.acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import mod.acats.fromanotherworld.entity.interfaces.TentacleThing;
import mod.acats.fromanotherworld.entity.render.thing.growths.Tentacle;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbsorberThing extends Thing implements TentacleThing {
    public static final int ABSORB_TIME = 120;

    public static final Predicate<LivingEntity> STANDARD = (livingEntity) ->
            (livingEntity.getType().is(EntityTags.HUMANOIDS) || livingEntity.getType().is(EntityTags.QUADRUPEDS)) &&
                    !((PossibleDisguisedThing) livingEntity).faw$isAssimilated();

    private static final EntityDataAccessor<Integer> ABSORB_PROGRESS;
    private static final EntityDataAccessor<Integer> ABSORB_TARGET_ID;
    public final List<Tentacle> absorbTentacles;

    @Override
    public float tentacleOriginOffset() {
        return this.getBbHeight() * 0.5F;
    }

    protected AbsorberThing(EntityType<? extends AbsorberThing> entityType, Level world) {
        super(entityType, world);
        absorbTentacles = new ArrayList<>();
        for (int i = 0; i < 25; i++){
            absorbTentacles.add(new Tentacle(this,
                    60,
                    new Vec3(this.getRandom().nextDouble() - 0.5D, this.getRandom().nextDouble(), this.getRandom().nextDouble() - 0.5D)));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ABSORB_PROGRESS, 0);
        this.entityData.define(ABSORB_TARGET_ID, 0);
    }

    public boolean absorbing() {
        LivingEntity absorbTarget = this.getAbsorbTarget();
        return absorbTarget != null && this.getAbsorbProgress() > 0 && absorbTarget.isAlive();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity absorbTarget = this.getAbsorbTarget();
        if (this.absorbing()){
            assert absorbTarget != null;
            this.tickAbsorb(absorbTarget);
        }
        else if (this.level().isClientSide()){
            for (Tentacle tentacle:
                    absorbTentacles) {
                tentacle.tick(null);
            }
        }
    }

    public void tickAbsorb(@NotNull LivingEntity victim){
        if (this.level().isClientSide()){
            for(int i = 0; i < this.getAbsorbProgress() / 10; ++i) {
                this.level().addParticle(ParticleRegistry.THING_GORE, victim.getRandomX(0.6D), victim.getRandomY(), victim.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
            }
            for (Tentacle tentacle:
                    absorbTentacles) {
                tentacle.tick(victim);
            }
        }
        else{
            this.updateTether(victim);
        }
    }

    private void updateTether(@NotNull LivingEntity victim){
        float f = this.distanceTo(victim);
        if (f > 2.0F){
            double d = (this.getX() - victim.getX()) / (double)f;
            double e = (this.getY() - victim.getY()) / (double)f;
            double g = (this.getZ() - victim.getZ()) / (double)f;
            victim.setDeltaMovement(victim.getDeltaMovement().add(Math.copySign(d * d * 0.1, d), Math.copySign(e * e * 0.1, e), Math.copySign(g * g * 0.1, g)));
        }
    }

    public void setAbsorbTarget(LivingEntity absorbTarget){
        this.entityData.set(ABSORB_TARGET_ID, absorbTarget.getId());
    }
    public void setAbsorbTargetID(int absorbTargetID){
        this.entityData.set(ABSORB_TARGET_ID, absorbTargetID);
    }

    @Nullable
    public LivingEntity getAbsorbTarget(){
        return (LivingEntity) this.level().getEntity(this.getAbsorbTargetID());
    }
    public int getAbsorbTargetID(){
        return this.entityData.get(ABSORB_TARGET_ID);
    }

    public void setAbsorbProgress(int absorbProgress){
        this.entityData.set(ABSORB_PROGRESS, absorbProgress);
    }

    public int getAbsorbProgress(){
        return this.entityData.get(ABSORB_PROGRESS);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target.equals(this.getAbsorbTarget()))
            return false;
        return super.canAttack(target);
    }

    public abstract void grow(LivingEntity otherParent);

    public void growInto(EntityType<? extends Thing> next){
        if (next != null){
            Thing nextThing = next.create(this.level());
            if (nextThing != null){
                nextThing.setPos(this.position());
                nextThing.initializeFrom(this);
                this.level().addFreshEntity(nextThing);
                nextThing.grief(0, 1);
            }
        }
        this.discard();
    }

    @Override
    public boolean canBurrow() {
        return super.canBurrow() && !this.absorbing();
    }

    @Override
    public boolean canDisguise() {
        return super.canDisguise() && !this.absorbing();
    }

    static {
        ABSORB_PROGRESS = SynchedEntityData.defineId(AbsorberThing.class, EntityDataSerializers.INT);
        ABSORB_TARGET_ID = SynchedEntityData.defineId(AbsorberThing.class, EntityDataSerializers.INT);
    }
}
