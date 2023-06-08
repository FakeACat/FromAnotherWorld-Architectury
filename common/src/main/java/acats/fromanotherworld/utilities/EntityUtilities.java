package acats.fromanotherworld.utilities;

import acats.fromanotherworld.config.Classification;
import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.thing.ThingEntity;
import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import acats.fromanotherworld.tags.BlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static acats.fromanotherworld.tags.EntityTags.*;

public class EntityUtilities {
    public static boolean isThing(Entity e){
        return e instanceof MaybeThing maybeThing && maybeThing.isThing();
    }
    public static boolean assimilate(Entity e){
        return assimilate(e, 1.0F);
    }
    public static boolean assimilate(Entity e, float supercellConcentration){
        if (e instanceof PlayerEntity playerEntity){
            StatusEffectInstance statusEffectInstance = playerEntity.getStatusEffect(StatusEffectRegistry.SLOW_ASSIMILATION.get());
            if (statusEffectInstance != null){
                int amplifier = Math.min(statusEffectInstance.getAmplifier() + 1, 9);
                int time = Math.min(statusEffectInstance.getDuration() + 400, 1200);
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SLOW_ASSIMILATION.get(), time, amplifier, false, true));
            }
            else{
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SLOW_ASSIMILATION.get(), 400, 0, false, true));
            }
            return false;
        }
        if (canAssimilate(e)){
            PossibleDisguisedThing thing = (PossibleDisguisedThing) e;
            thing.setSupercellConcentration(thing.getSupercellConcentration() + supercellConcentration);
            if (e instanceof MobEntity e2){
                e2.setPersistent();
            }
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean canAssimilate(Entity e){
        return !isThing(e) && (e.getType().isIn(HUMANOIDS) ||
                e.getType().isIn(QUADRUPEDS) ||
                e.getType().isIn(LARGE_QUADRUPEDS) ||
                e.getType().isIn(VERY_LARGE_QUADRUPEDS) ||
                e.getType().isIn(MISC));
    }
    public static int numThingsInList(List<? extends LivingEntity> list){
        int t = 0;
        for (LivingEntity e:
                list) {
            if (((MaybeThing) e).isDistinctThing()){
                t++;
            }
        }
        return t;
    }
    public static int numAssimilablesInList(List<LivingEntity> list){
        int t = 0;
        for (LivingEntity e:
                list) {
            if (canAssimilate(e)){
                t++;
            }
        }
        return t;
    }

    public static void spawnOnEntity(LivingEntity entity, World world, Entity targetEntity, int range){
        Random random = new Random();
        double x = targetEntity.getX() + random.nextInt(range * 2 + 1) - range;
        double z = targetEntity.getZ() + random.nextInt(range * 2 + 1) - range;
        double y = world.getTopY(Heightmap.Type.WORLD_SURFACE, (int)x, (int)z);
        entity.setPos(x, y, z);
        world.spawnEntity(entity);
    }

    public static boolean spawnOnEntityImproved(MobEntity entity, World world, Entity targetEntity, int minRangeH, int maxRangeH, int rangeV, int tries){
        for (int i = 0; i < tries; i++){
            int x = MathHelper.floor(targetEntity.getX()) + world.getRandom().nextBetween(minRangeH, maxRangeH) * (world.getRandom().nextBoolean() ? 1 : -1);
            int z = MathHelper.floor(targetEntity.getZ()) + world.getRandom().nextBetween(minRangeH, maxRangeH) * (world.getRandom().nextBoolean() ? 1 : -1);
            for (int y = MathHelper.floor(targetEntity.getY()) + world.getRandom().nextInt(rangeV); y > (int)targetEntity.getY() - rangeV; y--){
                if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial().blocksMovement()){
                    entity.setPosition(x + 0.5D, y, z + 0.5D);
                    if (world.isSpaceEmpty(entity) && !world.containsFluid(entity.getBoundingBox()))
                        return world.spawnEntity(entity);
                }
            }
        }
        return false;
    }

    public static void angerNearbyThings(int chance, LivingEntity entity, LivingEntity threat){
        double d = 16.0D;
        Box box = Box.from(entity.getPos()).expand(d, 10, d);
        List<LivingEntity> potentialThings = entity.world.getEntitiesByClass(LivingEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR);
        for (LivingEntity potentialThing:
                potentialThings) {
            if (entity.getRandom().nextInt(chance) == 0){
                if (potentialThing instanceof ThingEntity entity1 && threat != null){
                    entity1.currentThreat = threat;
                    if (entity1.canTarget(threat))
                        entity1.setTarget(threat);
                }
                else if (!potentialThing.equals(entity) && potentialThing instanceof PossibleDisguisedThing possibleDisguisedThing && possibleDisguisedThing.isAssimilated() && !possibleDisguisedThing.isRevealed() && !possibleDisguisedThing.isSleeper()){
                    CommonLivingEntityEvents.becomeResultant(potentialThing);
                }
            }
        }
    }

    public static boolean canThingDestroy(BlockState block){
        return !block.isAir() && !block.isIn(BlockTags.THING_IMMUNE) && block.getFluidState().isEmpty();
    }

    public static boolean isVulnerable(LivingEntity entity){
        for (StatusEffectInstance statusEffectInstance:
                entity.getStatusEffects()) {
            if (Classification.isRegenPreventative(statusEffectInstance))
                return true;
        }
        return entity.isOnFire();
    }

    public static void spawnAssimilatedPlayer(PlayerEntity playerEntity){
        World world = playerEntity.getWorld();
        PalmerThingEntity palmerThingEntity = EntityRegistry.PALMER_THING.get().create(world); //placeholder, assimilated player would be very cool
        if (palmerThingEntity != null){
            palmerThingEntity.setPosition(playerEntity.getPos());
            palmerThingEntity.setCustomName(playerEntity.getName());
            world.spawnEntity(palmerThingEntity);
        }
    }

    public static boolean canSee(Entity observer, Entity entity){
        if (entity.world != observer.world) {
            return false;
        } else {
            Vec3d vec3d = new Vec3d(observer.getX(), observer.getEyeY(), observer.getZ());
            Vec3d vec3d2 = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
            if (vec3d2.squaredDistanceTo(vec3d) > 16384.0F) {
                return false;
            } else {
                return observer.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, observer)).getType() == HitResult.Type.MISS;
            }
        }
    }
}
