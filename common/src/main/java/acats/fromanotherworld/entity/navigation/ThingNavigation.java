package acats.fromanotherworld.entity.navigation;

import acats.fromanotherworld.entity.thing.Thing;
import acats.fromanotherworld.registry.BlockRegistry;
import acats.fromanotherworld.utilities.BlockUtilities;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ThingNavigation extends AzureNavigation {
    private final Thing thing;
    public ThingNavigation(Thing thing, Level world) {
        super(thing, world);
        this.thing = thing;
        this.setCanWalkOverFences(true);
    }

    @Nullable
    private BlockPos pathToPosition;

    @Override
    public Path createPath(BlockPos blockPos, int i) {
        this.pathToPosition = blockPos;
        return super.createPath(blockPos, i);
    }

    @Override
    public Path createPath(Entity entity, int i) {
        this.pathToPosition = entity.blockPosition();
        return super.createPath(entity, i);
    }

    @Override
    public boolean moveTo(Entity entity, double d) {
        this.pathToPosition = entity.blockPosition();
        return super.moveTo(entity, d);
    }

    private int desiredX;
    private int desiredY;
    private int desiredZ;
    private boolean foundExit;
    private float exitDistSq;

    @Override
    public void tick() {
        if (this.path != null && !this.path.canReach() && this.thing.canBurrow()) {
            if (this.thing.getBurrowType() == Thing.BurrowType.REQUIRES_TUNNEL) {
                this.foundExit = false;
                BlockUtilities.getClosestBlock(this.path.getTarget(), 8, blockPos -> {
                    if (this.thing.level().getBlockState(blockPos).is(BlockRegistry.TUNNEL_BLOCK.get())) {

                        this.foundExit = true;

                        this.desiredX = blockPos.getX();
                        this.desiredY = blockPos.getY();
                        this.desiredZ = blockPos.getZ();

                        return true;
                    }
                    return false;
                });
                if (this.foundExit) {
                    this.exitDistSq = Float.MAX_VALUE;
                    BlockUtilities.forEachBlockInCubeCentredAt(this.thing.blockPosition(), 8, this::tryUseTunnel);
                }
                else if (this.thing.getRandom().nextInt(1280) == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    if (BlockUtilities.getClosestBlock(this.thing.blockPosition(), 8, blockPos -> this.thing.level().getBlockState(blockPos).is(BlockRegistry.TUNNEL_BLOCK.get())).isEmpty()) {
                        BlockUtilities.tryPlaceTunnelAt(this.thing.level(), this.thing.blockPosition());
                    }

                    if (BlockUtilities.getClosestBlock(this.path.getTarget(), 8, blockPos -> this.thing.level().getBlockState(blockPos).is(BlockRegistry.TUNNEL_BLOCK.get())).isEmpty()) {
                        BlockUtilities.tryPlaceTunnelAt(this.thing.level(), this.path.getTarget());
                    }
                }
            }
            else if (this.thing.getRandom().nextInt(4) == 0 && this.thing.getBurrowType() == Thing.BurrowType.CAN_BURROW) {
                this.thing.burrowTo(this.path.getTarget().getX(), this.path.getTarget().getY(), this.path.getTarget().getZ());
            }
        }

        // Modified version of tick() in AzureNavigation with a fix for entities spinning
        this.pathNavigationTick();
        if (this.isDone()) {
            if (this.pathToPosition != null) {
                double d = Math.max(1.0D, this.mob.getBbWidth());
                if (this.pathToPosition.closerToCenterThan(this.mob.position(), d) || this.mob.getY() > (double)this.pathToPosition.getY() && BlockPos.containing(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ()).closerToCenterThan(this.mob.position(), d)) {
                    this.pathToPosition = null;
                } else {
                    this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
                }
            }
            return;
        }
        if (this.getTargetPos() != null) {
            this.mob.getLookControl().setLookAt(this.getTargetPos().getX(), this.getTargetPos().getY(), this.getTargetPos().getZ());
        }
    }

    private void tryUseTunnel(BlockPos pos) {
        if (!this.level.getBlockState(pos).is(BlockRegistry.TUNNEL_BLOCK.get())) {
            return;
        }

        if (pos.equals(new BlockPos(this.desiredX, this.desiredY, this.desiredZ))) {
            return;
        }

        float sq = (float) this.thing.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
        if (this.exitDistSq <= sq) {
            return;
        }
        this.exitDistSq = sq;

        if (this.exitDistSq < 2.25) {
            this.thing.randomTeleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, false);
            this.thing.burrowTo(this.desiredX, this.desiredY, this.desiredZ);
        }

        Path path1 = this.createPath(pos, 1);

        if (path1 != null && path1.canReach()) {
            this.path = path1;
            this.thing.getMoveControl().setWantedPosition(path1.getTarget().getX(), path1.getTarget().getY(), path1.getTarget().getZ(), this.speedModifier);
        }
    }

    public void pathNavigationTick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            Vec3 vec3;
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                vec3 = this.getTempMobPos();
                Vec3 vec32 = this.path.getNextEntityPos(this.mob);
                if (vec3.y > vec32.y && !this.mob.onGround() && Mth.floor(vec3.x) == Mth.floor(vec32.x) && Mth.floor(vec3.z) == Mth.floor(vec32.z)) {
                    this.path.advance();
                }
            }

            DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                assert this.path != null;
                vec3 = this.path.getNextEntityPos(this.mob);
                this.mob.getMoveControl().setWantedPosition(vec3.x, this.getGroundY(vec3), vec3.z, this.speedModifier);
            }
        }
    }
}
