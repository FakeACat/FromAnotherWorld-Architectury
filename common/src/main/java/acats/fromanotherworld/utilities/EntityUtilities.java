package acats.fromanotherworld.utilities;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.entity.interfaces.MaybeThing;
import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.entity.thing.resultant.PalmerThing;
import acats.fromanotherworld.events.CommonLivingEntityEvents;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.StatusEffectRegistry;
import acats.fromanotherworld.tags.BlockTags;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import static acats.fromanotherworld.tags.EntityTags.*;

public class EntityUtilities {
    public static boolean isThing(Entity e){
        return e instanceof MaybeThing maybeThing && maybeThing.faw$isThing();
    }
    public static boolean assimilate(Entity e){
        return assimilate(e, 1.0F);
    }
    public static boolean assimilate(Entity e, float supercellConcentration){
        if (e instanceof Player playerEntity){
            MobEffectInstance statusEffectInstance = playerEntity.getEffect(StatusEffectRegistry.SLOW_ASSIMILATION.get());
            if (statusEffectInstance != null){
                int amplifier = Math.min(statusEffectInstance.getAmplifier() + 1, 9);
                int time = Math.min(statusEffectInstance.getDuration() + 400, 1200);
                playerEntity.addEffect(new MobEffectInstance(StatusEffectRegistry.SLOW_ASSIMILATION.get(), time, amplifier, false, true));
            }
            else{
                playerEntity.addEffect(new MobEffectInstance(StatusEffectRegistry.SLOW_ASSIMILATION.get(), 400, 0, false, true));
            }
            return false;
        }
        if (canAssimilate(e)){
            PossibleDisguisedThing thing = (PossibleDisguisedThing) e;
            thing.faw$setSupercellConcentration(thing.faw$getSupercellConcentration() + supercellConcentration);
            if (e instanceof Mob e2){
                e2.setPersistenceRequired();
            }
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isAssimilableType(Entity e){
        return e.getType().is(HUMANOIDS) ||
                e.getType().is(QUADRUPEDS) ||
                e.getType().is(LARGE_QUADRUPEDS) ||
                e.getType().is(VERY_LARGE_QUADRUPEDS) ||
                e.getType().is(MISC);
    }
    public static boolean canAssimilate(Entity e){
        return !isThing(e) && isAssimilableType(e);
    }
    public static int numThingsInList(List<? extends LivingEntity> list){
        int t = 0;
        for (LivingEntity e:
                list) {
            if (((MaybeThing) e).faw$isDistinctThing()){
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

    public static void spawnOnEntity(LivingEntity entity, Level world, Entity targetEntity, int range){
        Random random = new Random();
        double x = targetEntity.getX() + random.nextInt(range * 2 + 1) - range;
        double z = targetEntity.getZ() + random.nextInt(range * 2 + 1) - range;
        double y = world.getHeight(Heightmap.Types.WORLD_SURFACE, (int)x, (int)z);
        entity.setPosRaw(x, y, z);
        world.addFreshEntity(entity);
    }

    public static boolean spawnOnEntityImproved(Mob entity, Level world, Entity targetEntity, int minRangeH, int maxRangeH, int rangeV, int tries){
        for (int i = 0; i < tries; i++){
            int x = Mth.floor(targetEntity.getX()) + world.getRandom().nextIntBetweenInclusive(minRangeH, maxRangeH) * (world.getRandom().nextBoolean() ? 1 : -1);
            int z = Mth.floor(targetEntity.getZ()) + world.getRandom().nextIntBetweenInclusive(minRangeH, maxRangeH) * (world.getRandom().nextBoolean() ? 1 : -1);
            for (int y = Mth.floor(targetEntity.getY()) + world.getRandom().nextInt(rangeV); y > (int)targetEntity.getY() - rangeV; y--){
                if (world.getBlockState(new BlockPos(x, y - 1, z)).blocksMotion()){
                    entity.setPos(x + 0.5D, y, z + 0.5D);
                    if (world.noCollision(entity) && !world.containsAnyLiquid(entity.getBoundingBox()))
                        return world.addFreshEntity(entity);
                }
            }
        }
        return false;
    }

    public static void angerNearbyThings(int chance, LivingEntity entity, LivingEntity threat){
        double d = 16.0D;
        AABB box = AABB.unitCubeFromLowerCorner(entity.position()).inflate(d, 10, d);
        List<LivingEntity> potentialThings = entity.level().getEntitiesOfClass(LivingEntity.class, box, EntitySelector.NO_SPECTATORS);
        for (LivingEntity potentialThing:
                potentialThings) {
            if (entity.getRandom().nextInt(chance) == 0){
                if (potentialThing instanceof Thing entity1 && threat != null){
                    entity1.currentThreat = threat;
                    if (entity1.canAttack(threat))
                        entity1.setTarget(threat);
                }
                else if (!potentialThing.equals(entity) && potentialThing.getRemovalReason() == null && potentialThing instanceof PossibleDisguisedThing possibleDisguisedThing && possibleDisguisedThing.faw$isAssimilated() && !possibleDisguisedThing.faw$isRevealed() && !possibleDisguisedThing.faw$isSleeper()){
                    CommonLivingEntityEvents.becomeResultant(potentialThing);
                }
            }
        }
    }

    public static boolean canThingDestroy(BlockPos blockPos, Level level){
        float maxHardness = Config.DIFFICULTY_CONFIG.maxGriefingHardness.get();

        if (maxHardness < 0) {
            return false;
        }

        BlockState blockState = level.getBlockState(blockPos);
        float hardness = blockState.getDestroySpeed(level, blockPos);

        if (hardness < 0 || hardness > maxHardness) {
            return false;
        }

        return !blockState.isAir() && !blockState.is(BlockTags.THING_IMMUNE) && blockState.getFluidState().isEmpty();
    }

    public static boolean isVulnerable(LivingEntity entity){
        for (MobEffectInstance statusEffectInstance:
                entity.getActiveEffects()) {
            ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(statusEffectInstance.getEffect());
            if (id != null && Config.EFFECT_CONFIG.regenCancelling.contains(id.toString()))
                return true;
        }
        return entity.isOnFire();
    }

    public static void spawnAssimilatedPlayer(Player playerEntity){
        Level world = playerEntity.level();
        PalmerThing palmerThingEntity = EntityRegistry.PALMER_THING.get().create(world); //placeholder, assimilated player would be very cool
        if (palmerThingEntity != null){
            palmerThingEntity.setPos(playerEntity.position());
            palmerThingEntity.setCustomName(playerEntity.getName());
            world.addFreshEntity(palmerThingEntity);
        }
    }

    public static boolean canSee(Entity observer, Entity entity){
        if (entity.level() != observer.level()) {
            return false;
        } else {
            Vec3 vec3d = new Vec3(observer.getX(), observer.getEyeY(), observer.getZ());
            Vec3 vec3d2 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            if (vec3d2.distanceToSqr(vec3d) > 16384.0F) {
                return false;
            } else {
                return observer.level().clip(new ClipContext(vec3d, vec3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, observer)).getType() == HitResult.Type.MISS;
            }
        }
    }

    public static List<LivingEntity> nearbyEntities(Level level, Vec3i pos, int distH, int distV){
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getX() - distH, pos.getY() - distV, pos.getZ() - distH, pos.getX() + distH, pos.getY() + distV, pos.getZ() + distH));
    }

    public static boolean isThingAlly(Entity e){
        return e.getType().is(THING_ALLIES) || isThing(e);
    }

    public static boolean couldEntityFit(Entity entity, double x, double y, double z) {
        float w = entity.getBbWidth() / 2;
        float h = entity.getBbHeight();
        for (VoxelShape voxelShape : entity.level().getBlockCollisions(entity, new AABB(x - w, y, z - w, x + w, y + h, z + w))) {
            if (!voxelShape.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
