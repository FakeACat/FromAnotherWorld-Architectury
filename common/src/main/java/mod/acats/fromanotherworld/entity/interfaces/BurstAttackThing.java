package mod.acats.fromanotherworld.entity.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface BurstAttackThing {
    void shootBurst(LivingEntity target);
    boolean canShootBurst();
}
