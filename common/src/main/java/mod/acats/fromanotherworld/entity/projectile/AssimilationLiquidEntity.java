package mod.acats.fromanotherworld.entity.projectile;

import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class AssimilationLiquidEntity extends ThrowableItemProjectile {

    public AssimilationLiquidEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public AssimilationLiquidEntity(Level world, LivingEntity owner) {
        super(EntityRegistry.ASSIMILATION_LIQUID.get(), owner, world);
    }

    public AssimilationLiquidEntity(Level world, double x, double y, double z) {
        super(EntityRegistry.ASSIMILATION_LIQUID.get(), x, y, z, world);
    }
    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegistry.ASSIMILATION_LIQUID.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide()){
            Entity e = entityHitResult.getEntity();
            if (EntityUtilities.assimilate(e)){
                this.discard();
            }
            else if (!EntityUtilities.isThingAlly(e) && this.getOwner() != null){
                e.hurt(this.level().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 3.0F);
                this.discard();
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide && hitResult.getType() != HitResult.Type.ENTITY) {
            this.discard();
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double d = packet.getXa();
        double e = packet.getYa();
        double f = packet.getZa();
        float g = 0.25F;
        for(int i = 0; i < 7; ++i) {
            this.level().addParticle(ParticleRegistry.THING_SPIT.get(), this.getX(), this.getY(), this.getZ(), d + (this.random.nextFloat() - 0.5F) * g, e + (this.random.nextFloat() - 0.5F) * g, f + (this.random.nextFloat() - 0.5F) * g);
        }
    }
}
