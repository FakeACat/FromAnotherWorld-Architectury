package mod.acats.fromanotherworld.block.entity;

import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AssimilatedSculkBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);
    public AssimilatedSculkBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract void serverTick(Level level, BlockPos blockPos, BlockState blockState);
    public abstract void clientTick(Level level, BlockPos blockPos, BlockState blockState);

    public @Nullable Player getClosestVisible(double range) {
        if (this.level == null) {
            return null;
        }
        Player p = null;
        double distSq = range * range;
        Vec3 pos = new Vec3(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D);
        for (Player player:
             level.players()) {
            double d = player.distanceToSqr(pos);
            if (d < distSq && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && this.isVisible(player, pos)) {
                distSq = d;
                p = player;
            }
        }
        return p;
    }

    public boolean isVisible(Player player, Vec3 pos) {
        assert this.level != null;
        return this.level.clip(new ClipContext(pos, player.position().add(0.0D, player.getBbHeight() * 0.5D, 0.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
}
