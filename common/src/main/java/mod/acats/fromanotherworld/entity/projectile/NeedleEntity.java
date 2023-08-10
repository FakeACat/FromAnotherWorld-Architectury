package mod.acats.fromanotherworld.entity.projectile;

import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

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
            if (!EntityUtilities.assimilate(e) && !EntityUtilities.isThingAlly(e)){
                e.hurt(this.level().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 12.0F);
                this.discard();
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemRegistry.ASSIMILATION_LIQUID.get().getDefaultInstance();
    }
}
