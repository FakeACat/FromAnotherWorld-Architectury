package acats.fromanotherworld.entity;

import net.minecraft.entity.LivingEntity;

public interface BurstAttackThing {
    void shootBurst(LivingEntity target);
    boolean canShootBurst();
}
