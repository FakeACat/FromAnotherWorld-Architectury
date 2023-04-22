package acats.fromanotherworld.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.DamageTypeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommonEntityEvents {
    public static void afterServerEntityDeath(LivingEntity entity, DamageSource damageSource){
        if (entity instanceof PlayerEntity playerEntity && damageSource.isOf(DamageTypeRegistry.ASSIMILATION)){
            FromAnotherWorld.spawnAssimilatedPlayer(playerEntity);
        }
    }
}
