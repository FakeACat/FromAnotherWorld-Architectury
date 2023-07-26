package acats.fromanotherworld.events;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.entity.interfaces.CoordinatedThing;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.entity.thing.TransitionEntity;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import acats.fromanotherworld.memory.Aggression;
import acats.fromanotherworld.memory.ThingBaseOfOperations;
import acats.fromanotherworld.registry.DamageTypeRegistry;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.tags.EntityTags;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class CommonLivingEntityEvents {
    public static void initGoals(Mob mob, GoalSelector goalSelector){
        if (!mob.getType().is(EntityTags.NOT_AFRAID_OF_THINGS) &&
                EntityUtilities.isAssimilableType(mob) &&
                mob instanceof PathfinderMob pathfinderMob){
            goalSelector.addGoal(0, new AvoidEntityGoal<>(pathfinderMob, Thing.class, 6.0F, 1.0F, 1.2F));
        }
    }

    public static void serverPlayerEntityDeath(Player playerEntity, DamageSource damageSource){
        if (damageSource.is(DamageTypeRegistry.ASSIMILATION)){
            EntityUtilities.spawnAssimilatedPlayer(playerEntity);
        }
    }

    public static void serverEntityDeath(LivingEntity entity, DamageSource damageSource){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.faw$isAssimilated()){
            Aggression aggression = ((CoordinatedThing) entity).faw$getAggression();
            if (aggression != Aggression.HIDING) {
                EntityUtilities.angerNearbyThings(aggression == Aggression.AGGRESSIVE ? 1 : 3, entity, damageSource.getEntity() instanceof LivingEntity e ? e : null);
            }
            ((CoordinatedThing) entity).faw$getDirector().ifPresent(ThingBaseOfOperations.AIDirector::threaten);
            becomeResultant(entity);
        }
    }

    private static int revealCooldown(CoordinatedThing coordinatedThing) {
        int value = 12000;
        value *= coordinatedThing.faw$getHunger().revealCooldownMultiplier;
        return value;
    }

    private static int resultantChance(CoordinatedThing coordinatedThing) {
        int value = 9000;
        value *= coordinatedThing.faw$getHunger().transformChanceMultiplier;
        value *= coordinatedThing.faw$getAggression().transformChanceMultiplier;
        return value;
    }

    public static void tick(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.faw$isAssimilated()){
            if (entity.level().getDifficulty() == Difficulty.PEACEFUL){
                entity.discard();
                return;
            }
            CoordinatedThing coordinatedThing = ((CoordinatedThing) entity);
            if (entity.getRandom().nextInt(60) == 0) {
                coordinatedThing.faw$updateBase();
            }
            if (!thing.faw$isSleeper()){
                if (thing.faw$getRevealTimer() <= revealCooldown(coordinatedThing)){
                    thing.faw$setRevealTimer(thing.faw$getRevealTimer() + 1);
                }
                if (thing.faw$getRevealTimer() > revealCooldown(coordinatedThing) && entity.getRandom().nextInt(60) == 0){
                    tryReveal(entity);
                }
                if (entity.getRandom().nextInt(resultantChance(coordinatedThing)) == 0) {
                    tryBecomeResultant(entity);
                }
                thing.faw$setRevealed(Math.max(thing.faw$getRevealed() - 1, 0));
            }
        }
        else{
            if (thing.faw$getSupercellConcentration() > 0){
                if (entity.level().getDifficulty() == Difficulty.PEACEFUL){
                    entity.discard();
                    return;
                }
                thing.faw$setSupercellConcentration(thing.faw$getSupercellConcentration() * 1.005F);
                if (thing.faw$getSupercellConcentration() >= 100){
                    thing.faw$setAssimilated(true);
                    thing.faw$setSupercellConcentration(0);
                    onAssimilation(entity);
                }
                if (thing.faw$getSupercellConcentration() >= 1.0F){
                    if (!entity.level().isClientSide() && !EntityUtilities.isVulnerable(entity)){
                        entity.heal(1.0F);
                    }
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                }
            }
        }
    }

    private static void onAssimilation(LivingEntity entity) {
        if (entity instanceof Mob mobEntity)
            mobEntity.setTarget(null);

        setRareAbilities(entity, Config.DIFFICULTY_CONFIG.specialBehaviourRarity.get());

        CoordinatedThing coordinatedThing = ((CoordinatedThing) entity);
        coordinatedThing.faw$updateBase();
        coordinatedThing.faw$getDirector().ifPresent(ThingBaseOfOperations.AIDirector::successfulAssimilation);
    }

    public static void tickMovement(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (entity.level().isClientSide() && thing.faw$getSupercellConcentration() >= 1.0F){
            for(int i = 0; i < thing.faw$getSupercellConcentration() / 10; ++i) {
                entity.level().addParticle(ParticleRegistry.THING_GORE, entity.getRandomX(0.6D), entity.getRandomY(), entity.getRandomZ(0.6D), 0, 0, 0);
            }
        }
    }

    public static boolean canTarget(LivingEntity entity, LivingEntity target){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (!thing.faw$isAssimilated())
            return true;
        return !EntityUtilities.isThing(target);
    }

    public static void pushAway(LivingEntity entity, Entity other){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.faw$isAssimilated() && entity.getRandom().nextInt(6000) == 0){
            EntityUtilities.assimilate(other, 0.01F);
        }
    }

    public static void damage(LivingEntity entity, DamageSource damageSource){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.faw$isSleeper() && damageSource.getEntity() instanceof Player player && !entity.level().isClientSide()){
            if (((CoordinatedThing) entity).faw$getAggression() != Aggression.HIDING) {
                EntityUtilities.angerNearbyThings(1, entity, player);
            }
            for (int i = 0; i < 20; i++){
                AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ());
                assimilationLiquid.setDeltaMovement(new Vec3(entity.getRandom().nextDouble() - 0.5f, entity.getRandom().nextDouble(), entity.getRandom().nextDouble() - 0.5f));
                entity.level().addFreshEntity(assimilationLiquid);
            }
            becomeResultant(entity);
        }
    }


    private static void setRareAbilities(LivingEntity entity, int chanceDenominator){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        thing.faw$setSleeper(entity.getRandom().nextInt(chanceDenominator) == 0);
    }



    private static void tryReveal(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        int entityCheckDistH = 12;
        int entityCheckDistV = 2;
        int playerCheckDist = 32;
        Player p = entity.level().getNearestPlayer(entity, playerCheckDist);
        if ((p == null || p.isSpectator() || p.isCreative()) && !entity.level().isClientSide()){
            List<LivingEntity> nearbyEntities = entity.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(entity.getX() - entityCheckDistH, entity.getY() - entityCheckDistV, entity.getZ() - entityCheckDistH, entity.getX() + entityCheckDistH, entity.getY() + entityCheckDistV, entity.getZ() + entityCheckDistH),
                    (entity2) -> EntityUtilities.canSee(entity2, entity));
            int assimilables = EntityUtilities.numAssimilablesInList(nearbyEntities);
            int things = EntityUtilities.numThingsInList(nearbyEntities);
            if (assimilables > 0 && assimilables < (2 + things * 3)){
                reveal(entity);
                thing.faw$setRevealTimer(0);
            }
        }
    }

    private static void tryBecomeResultant(LivingEntity entity){
        if (entity.level().isClientSide()) {
            return;
        }
        if (((CoordinatedThing) entity).faw$getAggression() == Aggression.AGGRESSIVE || entity.level().getRandom().nextInt(7) == 0) {
            becomeResultant(entity);
            return;
        }
        int entityCheckDist = 16;
        List<LivingEntity> nearbyEntities = entity.level().getEntitiesOfClass(LivingEntity.class, new AABB(entity.getX() - entityCheckDist, entity.getY() - entityCheckDist, entity.getZ() - entityCheckDist, entity.getX() + entityCheckDist, entity.getY() + entityCheckDist, entity.getZ() + entityCheckDist));
        int assimilables = EntityUtilities.numAssimilablesInList(nearbyEntities);
        int things = EntityUtilities.numThingsInList(nearbyEntities);
        if (things > 4 && assimilables <= 1){
            becomeResultant(entity);
        }
    }

    private static void reveal(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        thing.faw$setTimeUntilFinishedRevealing(400);
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 6, false, false));
        ChestSpitter chestSpitter = EntityRegistry.CHEST_SPITTER.get().create(entity.level());
        if (chestSpitter != null){
            chestSpitter.setPos(entity.position());
            chestSpitter.host = entity;
            entity.level().addFreshEntity(chestSpitter);
        }
    }

    public static void becomeResultant(LivingEntity entity){
        if (entity.level().isClientSide()) {
            return;
        }
        TransitionEntity.createFrom(entity);
        entity.discard();
    }
}
