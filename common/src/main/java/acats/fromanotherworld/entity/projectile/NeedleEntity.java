package acats.fromanotherworld.entity.projectile;

import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class NeedleEntity extends AbstractArrow {

    public NeedleEntity(Level world, double x, double y, double z, LivingEntity owner) {
        super(EntityRegistry.NEEDLE.get(), owner, world);
        this.setPos(x, y, z);
    }

    public NeedleEntity(EntityType<NeedleEntity> needleEntityEntityType, Level world) {
        super(needleEntityEntityType, world);
    }

    public NeedleEntity(Level world, LivingEntity owner) {
        super(EntityRegistry.NEEDLE.get(), owner, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.level().isClientSide()){
            Entity e = entityHitResult.getEntity();
            if (!EntityUtilities.assimilate(e) && !EntityUtilities.isThing(e)){
                e.hurt(this.level().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 12.0F);
                this.discard();
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemRegistry.ASSIMILATION_LIQUID.get().getDefaultInstance();
    }
}
