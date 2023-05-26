package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.thing.ThingEntity;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.tags.EntityTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class AbsorberThingEntity extends ThingEntity {
    public static final int ABSORB_TIME = 120;

    public static final Predicate<LivingEntity> STANDARD = (livingEntity) ->
            (livingEntity.getType().isIn(EntityTags.HUMANOIDS) || livingEntity.getType().isIn(EntityTags.QUADRUPEDS)) &&
                    !((PossibleDisguisedThing) livingEntity).isAssimilated();

    private static final TrackedData<Integer> ABSORB_PROGRESS;
    private static final TrackedData<Integer> ABSORB_TARGET_ID;
    protected AbsorberThingEntity(EntityType<? extends AbsorberThingEntity> entityType, World world, boolean canHaveSpecialAbilities) {
        super(entityType, world, canHaveSpecialAbilities);
    }
    protected AbsorberThingEntity(EntityType<? extends AbsorberThingEntity> entityType, World world){
        this(entityType, world, true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ABSORB_PROGRESS, 0);
        this.dataTracker.startTracking(ABSORB_TARGET_ID, 0);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity absorbTarget = this.getAbsorbTarget();
        if (absorbTarget != null && this.getAbsorbProgress() > 0 && absorbTarget.isAlive()){
            this.tickAbsorb(absorbTarget);
        }
    }

    public void tickAbsorb(@NotNull LivingEntity victim){
        if (this.getWorld().isClient()){
            for(int i = 0; i < this.getAbsorbProgress() / 10; ++i) {
                double random = this.getRandom().nextDouble();
                Vec3d start = this.getPos().add(0, this.getHeight() / 2, 0);
                Vec3d finish = victim.getPos().add(0, victim.getHeight() / 2, 0);
                Vec3d pos = new Vec3d(MathHelper.lerp(random, start.x, finish.x), MathHelper.lerp(random, start.y, finish.y), MathHelper.lerp(random, start.z, finish.z));
                Vec3d vel = start.subtract(finish).normalize().multiply(0.5F);
                this.getWorld().addParticle(ParticleRegistry.THING_GORE, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
                this.getWorld().addParticle(ParticleRegistry.THING_GORE, victim.getParticleX(0.6D), victim.getRandomBodyY(), victim.getParticleZ(0.6D), vel.x, vel.y, vel.z);
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
            victim.setVelocity(victim.getVelocity().add(Math.copySign(d * d * 0.4, d), Math.copySign(e * e * 0.4, e), Math.copySign(g * g * 0.4, g)));
        }
    }

    public void setAbsorbTarget(LivingEntity absorbTarget){
        this.dataTracker.set(ABSORB_TARGET_ID, absorbTarget.getId());
    }
    public void setAbsorbTargetID(int absorbTargetID){
        this.dataTracker.set(ABSORB_TARGET_ID, absorbTargetID);
    }

    @Nullable
    public LivingEntity getAbsorbTarget(){
        return (LivingEntity) this.getWorld().getEntityById(this.getAbsorbTargetID());
    }
    public int getAbsorbTargetID(){
        return this.dataTracker.get(ABSORB_TARGET_ID);
    }

    public void setAbsorbProgress(int absorbProgress){
        this.dataTracker.set(ABSORB_PROGRESS, absorbProgress);
    }

    public int getAbsorbProgress(){
        return this.dataTracker.get(ABSORB_PROGRESS);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if (target.equals(this.getAbsorbTarget()))
            return false;
        return super.canTarget(target);
    }

    public static void defaultGrow(LivingEntity entity, EntityType<? extends ThingEntity> next){
        if (next != null){
            ThingEntity nextThing = next.create(entity.getWorld());
            if (nextThing != null){
                nextThing.setPosition(entity.getPos());
                if (nextThing instanceof MinibossThingEntity miniboss)
                    miniboss.setTier(0, true);
                entity.getWorld().spawnEntity(nextThing);
                if (entity.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
                    nextThing.grief(0, 1);
            }
        }
        entity.discard();
    }

    static {
        ABSORB_PROGRESS = DataTracker.registerData(AbsorberThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        ABSORB_TARGET_ID = DataTracker.registerData(AbsorberThingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
