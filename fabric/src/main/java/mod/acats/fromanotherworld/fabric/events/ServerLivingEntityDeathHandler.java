package mod.acats.fromanotherworld.fabric.events;

import mod.acats.fromanotherworld.events.CommonLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ServerLivingEntityDeathHandler implements ServerLivingEntityEvents.AfterDeath{
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof Player player)
            CommonLivingEntityEvents.serverPlayerEntityDeath(player, damageSource);
    }
}
