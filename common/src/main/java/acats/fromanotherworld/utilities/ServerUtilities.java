package acats.fromanotherworld.utilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class ServerUtilities {
    private static TargetingConditions suitablePlayers(int range){
        return TargetingConditions.forNonCombat()
                .range(range)
                .ignoreInvisibilityTesting()
                .ignoreLineOfSight();
    }

    public static void forAllPlayersNearEntity(LivingEntity entity, int range, Consumer<Player> consumer){
        Level level = entity.level();
        if (!level.isClientSide()){
            Vec3 position = entity.position();
            level.getNearbyPlayers(suitablePlayers(range),
                    entity,
                    new AABB(position.x() - range,
                            position.y() - range,
                            position.z() - range,
                            position.x() + range,
                            position.y() + range,
                            position.z() + range)).forEach(consumer);
        }
    }
}
