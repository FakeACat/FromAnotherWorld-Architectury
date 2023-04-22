package acats.fromanotherworld.fabric.event;

import acats.fromanotherworld.events.CommonEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class ServerLivingEntityDeathHandler implements ServerLivingEntityEvents.AfterDeath{
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        CommonEntityEvents.afterServerEntityDeath(entity, damageSource);
    }
}
