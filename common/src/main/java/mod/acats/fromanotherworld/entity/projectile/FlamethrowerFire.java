package mod.acats.fromanotherworld.entity.projectile;

import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FlamethrowerFire extends ThrowableProjectile {

    public static final int MAX_AGE = 10;

    public FlamethrowerFire(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FlamethrowerFire(EntityType<? extends ThrowableProjectile> entityType, LivingEntity livingEntity, Level level) {
        super(entityType, livingEntity, level);
    }

    public FlamethrowerFire(Level level, LivingEntity livingEntity) {
        this(EntityRegistry.FLAMETHROWER_FIRE.get(), livingEntity, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(0.5D), this.getZ(), 0.0D, 0.0D, 0.0D);
            float f = (float) this.tickCount / MAX_AGE;
            if (this.random.nextFloat() < f) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(0.5D), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
            if (this.random.nextFloat() < f * f) {
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(0.5D), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        else {
            if (this.tickCount > MAX_AGE) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide()) {
            Entity e = entityHitResult.getEntity();
            e.setSecondsOnFire(16);
            e.hurt(this.level().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 3.0F);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            BlockUtilities.forEachBlockInCubeCentredAt(this.blockPosition(), 1, pos -> {
                if (this.level().getBlockState(pos).is(Blocks.AIR)) {
                    this.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                }
            });
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
    }
}
