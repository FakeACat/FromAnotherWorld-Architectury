package acats.fromanotherworld.entity.projectile;

import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class NeedleEntity extends PersistentProjectileEntity {

    public NeedleEntity(World world, double x, double y, double z, LivingEntity owner) {
        super(EntityRegistry.NEEDLE.get(), owner, world);
        this.setPosition(x, y, z);
    }

    public NeedleEntity(EntityType<NeedleEntity> needleEntityEntityType, World world) {
        super(needleEntityEntityType, world);
    }

    public NeedleEntity(World world, LivingEntity owner) {
        super(EntityRegistry.NEEDLE.get(), owner, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient()){
            Entity e = entityHitResult.getEntity();
            if (!EntityUtilities.assimilate(e) && !EntityUtilities.isThing(e)){
                e.damage(this.getWorld().getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 12.0F);
                this.discard();
            }
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemRegistry.ASSIMILATION_LIQUID.get().getDefaultStack();
    }
}
