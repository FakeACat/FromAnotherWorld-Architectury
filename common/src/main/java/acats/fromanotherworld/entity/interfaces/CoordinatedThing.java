package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.memory.Aggression;
import acats.fromanotherworld.memory.GlobalThingMemory;
import acats.fromanotherworld.memory.ThingBaseOfOperations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface CoordinatedThing {
    @Nullable ThingBaseOfOperations faw$getBase();
    void faw$setBase(@Nullable ThingBaseOfOperations base);
    default @Nullable ThingBaseOfOperations.AIDirector faw$getDirector() {
        return this.faw$hasBase() ? Objects.requireNonNull(this.faw$getBase()).director : null;
    }
    default boolean faw$hasBase() {
        return this.faw$getBase() != null;
    }
    default void faw$updateBase() {
        Entity entity = (Entity) this;
        if (entity.level().isClientSide() || (this.faw$hasBase() && entity.level().getRandom().nextInt(20) != 0)) {
            return;
        }
        GlobalThingMemory globalThingMemory = GlobalThingMemory.getGlobalThingMemory((ServerLevel) entity.level());
        ThingBaseOfOperations closest = globalThingMemory.closestBase((int) entity.getX(), (int) entity.getY(), (int) entity.getZ());
        if (closest != null) {
            this.faw$setBase(closest);
        }
        else {
            this.faw$setBase(globalThingMemory.tryCreateBase((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()));
        }
    }
    default Aggression faw$getAggression() {
        return this.faw$hasBase() ? Objects.requireNonNull(this.faw$getDirector()).getAggression() : Aggression.NORMAL;
    }
}
