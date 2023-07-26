package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.memory.Aggression;
import acats.fromanotherworld.memory.GlobalThingMemory;
import acats.fromanotherworld.memory.Hunger;
import acats.fromanotherworld.memory.ThingBaseOfOperations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface CoordinatedThing {

    Optional<ThingBaseOfOperations> faw$getBase();

    void faw$setBase(@Nullable ThingBaseOfOperations base);

    default Optional<ThingBaseOfOperations.AIDirector> faw$getDirector() {
        return this.faw$getBase().isPresent() ? Optional.of(this.faw$getBase().get().director) : Optional.empty();
    }

    default void faw$updateBase() {
        Entity entity = (Entity) this;
        if (entity.level().isClientSide() || (this.faw$getBase().isPresent() && entity.level().getRandom().nextInt(20) != 0)) {
            return;
        }

        GlobalThingMemory globalThingMemory = GlobalThingMemory.getGlobalThingMemory((ServerLevel) entity.level());
        globalThingMemory.closestBase((int) entity.getX(), (int) entity.getY(), (int) entity.getZ())
                .ifPresentOrElse(
                        this::faw$setBase,
                        () -> this.faw$setBase(globalThingMemory.tryCreateBase((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()))
                );
    }

    default Aggression faw$getAggression() {
        return this.faw$getDirector().isPresent() ? this.faw$getDirector().get().getAggression() : Aggression.NORMAL;
    }

    default Hunger faw$getHunger() {
        return this.faw$getDirector().isPresent() ? this.faw$getDirector().get().getHunger() : Hunger.NORMAL;
    }
}
