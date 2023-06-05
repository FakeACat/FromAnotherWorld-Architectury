package acats.fromanotherworld.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.General;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.thing.TransitionEntity;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import acats.fromanotherworld.registry.DamageTypeRegistry;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;

import java.util.List;

public class CommonLivingEntityEvents {
    private static final int REVEAL_COOLDOWN = 12000;
    public static void serverPlayerEntityDeath(PlayerEntity playerEntity, DamageSource damageSource){
        if (damageSource.isOf(DamageTypeRegistry.ASSIMILATION)){
            FromAnotherWorld.spawnAssimilatedPlayer(playerEntity);
        }
    }
    public static void serverEntityDeath(LivingEntity entity, DamageSource damageSource){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.isAssimilated()){
            FromAnotherWorld.angerNearbyThings(2, entity, damageSource.getAttacker() instanceof LivingEntity e ? e : null);
            becomeResultant(entity);
        }
    }

    public static void tick(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.isAssimilated()){
            if (entity.world.getDifficulty() == Difficulty.PEACEFUL){
                entity.discard();
                return;
            }
            if (!thing.isSleeper()){
                if (thing.getRevealTimer() <= REVEAL_COOLDOWN){
                    thing.setRevealTimer(thing.getRevealTimer() + 1);
                }
                if (thing.getRevealTimer() > REVEAL_COOLDOWN && entity.getRandom().nextInt(60) == 0){
                    tryReveal(entity);
                }
                if (entity.getRandom().nextInt(9000) == 0){
                    tryBecomeResultant(entity);
                }
                thing.setRevealed(Math.max(thing.getRevealed() - 1, 0));
            }
        }
        else{
            if (thing.getSupercellConcentration() > 0){
                if (entity.world.getDifficulty() == Difficulty.PEACEFUL){
                    entity.discard();
                    return;
                }
                thing.setSupercellConcentration(thing.getSupercellConcentration() * 1.005F);
                if (thing.getSupercellConcentration() >= 100){
                    thing.setAssimilated(true);
                    if (entity instanceof MobEntity mobEntity)
                        mobEntity.setTarget(null);
                    setRareAbilities(entity, General.specialBehaviourRarity);
                    thing.setSupercellConcentration(0);
                }
                if (thing.getSupercellConcentration() >= 1.0F){
                    if (!entity.world.isClient() && !FromAnotherWorld.isVulnerable(entity)){
                        entity.heal(1.0F);
                    }
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
                }
            }
        }
    }

    public static void tickMovement(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (entity.world.isClient() && thing.getSupercellConcentration() >= 1.0F){
            for(int i = 0; i < thing.getSupercellConcentration() / 10; ++i) {
                entity.world.addParticle(ParticleRegistry.THING_GORE, entity.getParticleX(0.6D), entity.getRandomBodyY(), entity.getParticleZ(0.6D), 0, 0, 0);
            }
        }
    }

    public static boolean canTarget(LivingEntity entity, LivingEntity target){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (!thing.isAssimilated())
            return true;
        return !FromAnotherWorld.isThing(target);
    }

    public static void pushAway(LivingEntity entity, Entity other){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.isAssimilated() && entity.getRandom().nextInt(6000) == 0){
            FromAnotherWorld.assimilate(other, 0.01F);
        }
    }

    public static void damage(LivingEntity entity, DamageSource damageSource){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        if (thing.isSleeper() && damageSource.getAttacker() instanceof PlayerEntity player && !entity.world.isClient()){
            FromAnotherWorld.angerNearbyThings(1, entity, player);
            for (int i = 0; i < 20; i++){
                AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(entity.world, entity.getX(), entity.getY(), entity.getZ());
                assimilationLiquid.setVelocity(new Vec3d(entity.getRandom().nextDouble() - 0.5f, entity.getRandom().nextDouble(), entity.getRandom().nextDouble() - 0.5f));
                entity.world.spawnEntity(assimilationLiquid);
            }
            becomeResultant(entity);
        }
    }


    private static void setRareAbilities(LivingEntity entity, int chanceDenominator){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        thing.setSleeper(entity.getRandom().nextInt(chanceDenominator) == 0);
    }



    private static void tryReveal(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        int entityCheckDistH = 12;
        int entityCheckDistV = 2;
        int playerCheckDist = 32;
        PlayerEntity p = entity.world.getClosestPlayer(entity, playerCheckDist);
        if ((p == null || p.isSpectator() || p.isCreative()) && !entity.world.isClient()){
            List<LivingEntity> nearbyEntities = entity.world.getEntitiesByClass(LivingEntity.class,
                    new Box(entity.getX() - entityCheckDistH, entity.getY() - entityCheckDistV, entity.getZ() - entityCheckDistH, entity.getX() + entityCheckDistH, entity.getY() + entityCheckDistV, entity.getZ() + entityCheckDistH),
                    (entity2) -> FromAnotherWorld.canSee(entity2, entity));
            int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
            int things = FromAnotherWorld.numThingsInList(nearbyEntities);
            if (assimilables > 0 && assimilables < (2 + things * 3)){
                reveal(entity);
                thing.setRevealTimer(0);
            }
        }
    }

    private static void tryBecomeResultant(LivingEntity entity){
        int entityCheckDist = 16;
        List<LivingEntity> nearbyEntities = entity.getWorld().getNonSpectatingEntities(LivingEntity.class, new Box(entity.getX() - entityCheckDist, entity.getY() - entityCheckDist, entity.getZ() - entityCheckDist, entity.getX() + entityCheckDist, entity.getY() + entityCheckDist, entity.getZ() + entityCheckDist));
        int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
        int things = FromAnotherWorld.numThingsInList(nearbyEntities);
        if (!entity.getWorld().isClient() && (entity.getRandom().nextInt(50) == 0 || (things > 4 && assimilables <= 1))){
            becomeResultant(entity);
        }
    }

    private static void reveal(LivingEntity entity){
        PossibleDisguisedThing thing = ((PossibleDisguisedThing) entity);
        thing.setTimeUntilFinishedRevealing(400);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 6, false, false));
        ChestSpitterEntity chestSpitterEntity = EntityRegistry.CHEST_SPITTER.get().create(entity.world);
        if (chestSpitterEntity != null){
            chestSpitterEntity.setPosition(entity.getPos());
            chestSpitterEntity.host = entity;
            entity.world.spawnEntity(chestSpitterEntity);
        }
    }

    public static void becomeResultant(LivingEntity entity){
        if (entity.world.isClient()) {
            return;
        }
        TransitionEntity.createFrom(entity);
        entity.discard();
    }
}
