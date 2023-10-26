package mod.acats.fromanotherworld.block.entity;

import mod.acats.fromanotherworld.block.spreading.AssimilatedSculkSpreader;
import mod.acats.fromanotherworld.constants.FAWAnimations;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.registry.DamageTypeRegistry;
import mod.acats.fromanotherworld.registry.SoundRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import mod.acats.fromanotherworld.utilities.physics.Chain;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssimilatedSculkTentaclesBlockEntity extends AssimilatedSculkBlockEntity {

    public static final int TENTACLE_SEGMENTS = 18;
    private static final double RANGE = 16;
    private final AssimilatedSculkSpreader simSculker = AssimilatedSculkSpreader.create();

    public Chain tentacle;
    public AssimilatedSculkTentaclesBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ASSIMILATED_SCULK_TENTACLES_BLOCK_ENTITY.get(), blockPos, blockState);
        Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D);
        this.desiredPos = pos;
        this.desiredPos2 = pos;
        this.tentacle = new Chain(pos,
                32, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 24, 24, 24);
        this.tentacle.updatePosition(pos, pos, pos, 360, true);
        this.target = null;
    }

    private Vec3 desiredPos;
    private Vec3 desiredPos2;
    private float speed;
    private LivingEntity target;

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState) {
        simSculker.updateCursors(level, blockPos, level.getRandom(), true);

        if (BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().revealed(blockState)) {
            this.revealedTick(level, blockPos);
        }

        if (level.getRandom().nextInt(3000) == 0) {
            this.simSculker.addCursors(blockPos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(level.getRandom())), 100);
        }

        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    private void revealedTick(Level level, BlockPos blockPos) {
        Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D);

        if (level.getRandom().nextInt(20) == 0) {
            LivingEntity prevTarget = this.target;
            this.target = level.getNearestEntity(LivingEntity.class,
                    TargetingConditions.forNonCombat().selector(Thing::hostileTowards),
                    null,
                    pos.x,
                    pos.y,
                    pos.z,
                    AABB.ofSize(pos, RANGE * 2.0D, RANGE, RANGE * 2.0D)
            );

            if (prevTarget != this.target) {
                Vec3 pos2 = this.tentacle.segments.get(0).getTipPos();
                level.playSound(null, pos2.x(), pos2.y(), pos2.z(), SoundRegistry.STRONG_ALERT.get(), SoundSource.BLOCKS, 1.0F, 0.2F);
            }
        }

        if (EntityUtilities.isThing(this.target)) {
            this.target = null;
        }

        boolean bl = target == null;

        if (bl) {
            this.speed = 0;
            if (this.desiredPos2.distanceToSqr(this.desiredPos) < 1.0D) {
                this.desiredPos2 = new Vec3(
                        (level.getRandom().nextDouble() - 0.5D) * 20.0D,
                        3.0D + level.getRandom().nextDouble() * 7.0D,
                        (level.getRandom().nextDouble() - 0.5D) * 20.0D
                ).add(pos);
            }
            this.desiredPos = lerpPos(0.02F, desiredPos, desiredPos2);
        } else {
            if (this.speed > 0.04F) {
                this.speed = 1.0F;
            } else {
                this.speed += 0.0005F;
            }
            this.desiredPos = lerpPos(speed, desiredPos.add(0.0D, 0.1D, 0.0D), target.position().add(0.0D, target.getBbHeight() / 2.0D, 0.0D));
        }

        Vec3 tip = this.tentacle.updatePosition(
                this.desiredPos,
                bl ? this.desiredPos.subtract(pos).multiply(2.0D, 0.0D, 2.0D).add(pos) : target.position().add(0.0D, target.getBbHeight() / 2.0D, 0.0D),
                pos,
                3.0F,
                true
        );

        if (!bl && tip.distanceToSqr(target.position().add(0.0D, target.getBbHeight() / 2.0D, 0.0D)) < 1.0D) {
            this.hitEntity(target);
            this.speed = 0;
        }
    }

    private void hitEntity(LivingEntity entity) {
        float damage = EntityUtilities.assimilate(entity) ? 0 : 10;
        entity.hurt(DamageTypeRegistry.assimilation(entity.level()), damage);
    }

    private Vec3 lerpPos(float f, Vec3 start, Vec3 finish) {
        return new Vec3(
                Mth.lerp(f, start.x, finish.x),
                Mth.lerp(f, start.y, finish.y),
                Mth.lerp(f, start.z, finish.z)
        );
    }

    public float[] pitch = new float[TENTACLE_SEGMENTS];
    public float[] prevPitch = new float[TENTACLE_SEGMENTS];
    public float[] yaw = new float[TENTACLE_SEGMENTS];
    public float[] prevYaw = new float[TENTACLE_SEGMENTS];
    public double[] x = new double[TENTACLE_SEGMENTS];
    public double[] prevX = new double[TENTACLE_SEGMENTS];
    public double[] y = new double[TENTACLE_SEGMENTS];
    public double[] prevY = new double[TENTACLE_SEGMENTS];
    public double[] z = new double[TENTACLE_SEGMENTS];
    public double[] prevZ = new double[TENTACLE_SEGMENTS];

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState) {
        if (!BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().revealed(blockState)) {
            return;
        }
        for (int i = 0; i < this.tentacle.segments.size(); i++) {
            prevPitch[i] = pitch[i];
            pitch[i] = this.tentacle.segments.get(i).pitchRadians();

            prevYaw[i] = yaw[i];
            yaw[i] = this.tentacle.segments.get(i).yawRadians();

            prevX[i] = x[i];
            x[i] = this.tentacle.segments.get(i).getTipPos().x();

            prevY[i] = y[i];
            y[i] = this.tentacle.segments.get(i).getTipPos().y();

            prevZ[i] = z[i];
            z[i] = this.tentacle.segments.get(i).getTipPos().z();
        }
        Vec3 pos = this.tentacle.segments.get(0).getTipPos();
        BlockPos blockPos1 = new BlockPos(Mth.floor(pos.x()), Mth.floor(pos.y()), Mth.floor(pos.z()));
        BlockState blockState1 = level.getBlockState(blockPos1);
        if (!blockState1.is(Blocks.AIR)) {
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FAWAnimations.blockAlwaysPlaying(this));
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putDouble("tip_x", desiredPos.x());
        compoundTag.putDouble("tip_y", desiredPos.y());
        compoundTag.putDouble("tip_z", desiredPos.z());

        this.tentacle.writeTo(compoundTag, "tentacle");

        this.simSculker.saveTo(compoundTag);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.desiredPos = new Vec3(compoundTag.getDouble("tip_x"), compoundTag.getDouble("tip_y"), compoundTag.getDouble("tip_z"));

        this.tentacle = Chain.readFrom(compoundTag, "tentacle");

        this.simSculker.loadFrom(compoundTag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}
