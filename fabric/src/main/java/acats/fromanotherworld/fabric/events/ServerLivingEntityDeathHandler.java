package acats.fromanotherworld.fabric.events;

import acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class ServerLivingEntityDeathHandler implements ServerLivingEntityEvents.AfterDeath{
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof PlayerEntity player)
            CommonLivingEntityEvents.serverPlayerEntityDeath(player, damageSource);
    }
}
