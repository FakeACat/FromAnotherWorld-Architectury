package mod.acats.fromanotherworld.entity.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface RespawnableThing {
    default void modifyRespawnData(CompoundTag tag) {
    }
    default void preAttemptRespawn(Player spawnOnPlayer) {
    }
    default void postRespawn(Player spawnOnPlayer) {
    }

    default int minHorizontalRange() {
        return 20;
    }
    default int maxHorizontalRange() {
        return 100;
    }
    default int verticalRange() {
        return 80;
    }
    default int verticalOffset() {
        return 0;
    }
}
