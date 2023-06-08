package acats.fromanotherworld.entity.projectile;

import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class AssimilationLiquidEntity extends ThrownItemEntity {

    public AssimilationLiquidEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public AssimilationLiquidEntity(World world, LivingEntity owner) {
        super(EntityRegistry.ASSIMILATION_LIQUID.get(), owner, world);
    }

    public AssimilationLiquidEntity(World world, double x, double y, double z) {
        super(EntityRegistry.ASSIMILATION_LIQUID.get(), x, y, z, world);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.ASSIMILATION_LIQUID.get();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient()){
            Entity e = entityHitResult.getEntity();
            if (EntityUtilities.assimilate(e)){
                this.discard();
            }
            else if (!EntityUtilities.isThing(e) && this.getOwner() != null){
                e.damage(this.getWorld().getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 3.0F);
                this.discard();
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient && hitResult.getType() != HitResult.Type.ENTITY) {
            this.discard();
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double d = packet.getVelocityX();
        double e = packet.getVelocityY();
        double f = packet.getVelocityZ();
        float g = 0.25F;
        for(int i = 0; i < 7; ++i) {
            this.getWorld().addParticle(ParticleRegistry.THING_SPIT, this.getX(), this.getY(), this.getZ(), d + (this.random.nextFloat() - 0.5F) * g, e + (this.random.nextFloat() - 0.5F) * g, f + (this.random.nextFloat() - 0.5F) * g);
        }
    }
}
