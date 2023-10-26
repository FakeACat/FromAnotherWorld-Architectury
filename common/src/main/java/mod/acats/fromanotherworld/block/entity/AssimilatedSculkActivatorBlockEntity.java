package mod.acats.fromanotherworld.block.entity;

import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.entity.misc.SculkRevealer;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssimilatedSculkActivatorBlockEntity extends AssimilatedSculkBlockEntity {
    public AssimilatedSculkActivatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ASSIMILATED_SCULK_ACTIVATOR_BLOCK_ENTITY.get(), blockPos, blockState);
        this.observed = null;
    }

    private @Nullable Player observed;
    private boolean setup;
    public float yaw;
    public float clientYaw;
    public float clientPrevYaw;

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState) {

        if (!setup) {
            setup = true;
            this.yaw = level.getRandom().nextInt(360);
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }

        if (!blockState.getValue(AssimilatedSculk.REVEALED)) {
            if (this.tryFindPlayer(level)) {
                SculkRevealer.create(level, blockPos, 0.25F, 128);
            }
            return;
        }

        this.tryFindPlayer(level);
        if (this.observed != null) {
            Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D);
            Vec3 offset = this.observed.position().subtract(pos);
            this.yaw = -Mth.wrapDegrees((float)(Mth.atan2(offset.z, offset.x) * 57.2957763671875) - 90.0F);
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    private boolean tryFindPlayer(Level level) {
        if (level.getRandom().nextInt(20) == 0) {
            Player player = this.getClosestVisiblePlayer(6);
            this.observed = player;
            return player != null;
        }
        return false;
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState) {
        this.clientPrevYaw = this.clientYaw;
        this.clientYaw = yaw;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putFloat("yaw", this.yaw);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.yaw = compoundTag.getFloat("yaw");
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
