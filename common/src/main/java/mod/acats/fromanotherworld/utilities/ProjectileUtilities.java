package mod.acats.fromanotherworld.utilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class ProjectileUtilities {
    public static <E extends Projectile> E shootFromTo(E projectile, LivingEntity shooter, LivingEntity target, float speed, Vec3 offset){
        projectile.setPos(new Vec3(projectile.getX(), projectile.getY(), projectile.getZ()).add(offset.yRot(-(float)Math.toRadians(shooter.yBodyRot + 90))));
        projectile.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(projectile.position()).normalize().multiply(speed, speed, speed));
        shooter.level().addFreshEntity(projectile);
        return projectile;
    }
}
