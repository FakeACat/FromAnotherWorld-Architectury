package mod.acats.fromanotherworld.block.entity;

import mod.acats.fromanotherworld.entity.interfaces.SimSculkObservable;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AssimilatedSculkBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache animatableInstanceCache = AzureLibUtil.createInstanceCache(this);
    public AssimilatedSculkBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract void serverTick(Level level, BlockPos blockPos, BlockState blockState);
    public abstract void clientTick(Level level, BlockPos blockPos, BlockState blockState);

    public @Nullable LivingEntity getClosestObservedEntity(double range) {
        if (this.level == null) {
            return null;
        }
        LivingEntity e = null;
        double distSq = range * range;
        Vec3 pos = new Vec3(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D);
        List<LivingEntity> entities = level.getEntities(EntityTypeTest.forClass(LivingEntity.class), AABB.ofSize(pos, range * 2.0D, range * 2.0D, range * 2.0D), Thing::hostileTowards);
        for (LivingEntity entity:
                entities) {
            double d = entity.distanceToSqr(pos);
            if (d < distSq && Thing.hostileTowards(entity) && ((SimSculkObservable) entity).faw$isObserved()) {
                distSq = d;
                e = entity;
            }
        }
        return e;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
}
