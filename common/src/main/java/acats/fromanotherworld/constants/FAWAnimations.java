package acats.fromanotherworld.constants;

import acats.fromanotherworld.entity.thing.ThingEntity;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;

import java.util.function.BiConsumer;

public final class FAWAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
    public static final RawAnimation FREEZING = RawAnimation.begin().thenPlayXTimes("misc.freezing", 1).thenPlayAndHold("misc.frozen");
    public static final RawAnimation FROZEN = RawAnimation.begin().thenPlayAndHold("misc.frozen");
    public static final RawAnimation SPAWN = RawAnimation.begin().thenPlayXTimes("misc.spawn", 1).thenLoop("misc.idle");

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("move.swim");
    public static final RawAnimation CHASE = RawAnimation.begin().thenLoop("move.chase");
    public static final RawAnimation BURROW = RawAnimation.begin().thenPlayAndHold("move.burrow");
    public static final RawAnimation EMERGE = RawAnimation.begin().thenPlayAndHold("move.emerge");

    private static <E extends ThingEntity> void frozen(AnimationState<E> event, E thing){
        if (thing.getCold() == 1.0F){
            event.getController().setAnimation(FROZEN);
        }
        else{
            event.getController().setAnimation(FREEZING);
        }
    }

    private static <E extends ThingEntity> void movement(AnimationState<E> event, E thing){
        if (thing.isAggressive()){
            event.getController().setAnimation(CHASE);
        }
        else{
            event.getController().setAnimation(WALK);
        }
    }

    private static <E extends ThingEntity> void generic(AnimationState<E> event,
                                                        E thing,
                                                        BiConsumer<AnimationState<E>, E> frozen,
                                                        BiConsumer<AnimationState<E>, E> movement,
                                                        BiConsumer<AnimationState<E>, E> idle){
        if (thing.isThingFrozen()){
            frozen.accept(event, thing);
        }
        else{
            if (event.isMoving() || (thing.rotateWhenClimbing() && thing.movingClimbing())){
                movement.accept(event, thing);
            }
            else{
                idle.accept(event, thing);
            }
        }
    }

    public static <E extends ThingEntity> AnimationController<E> genericThing(E thing,
                                                                              String name,
                                                                              BiConsumer<AnimationState<E>, E> frozen,
                                                                              BiConsumer<AnimationState<E>, E> movement,
                                                                              BiConsumer<AnimationState<E>, E> idle){
        return new AnimationController<>(thing, name, 5, (event) -> {
            generic(event, thing, frozen, movement, idle);
            return PlayState.CONTINUE;
        });
    }

    public static <E extends ThingEntity> AnimationController<E> defaultThing(E thing){
        return genericThing(thing,
                "DefaultThing",
                FAWAnimations::frozen,
                FAWAnimations::movement,
                (event2, thing2) -> event2.getController().setAnimation(IDLE));
    }

    public static <E extends ThingEntity> AnimationController<E> defaultThingNoChase(E thing){
        return genericThing(thing,
                "DefaultThingNoChase",
                FAWAnimations::frozen,
                (event2, thing2) -> event2.getController().setAnimation(WALK),
                (event2, thing2) -> event2.getController().setAnimation(IDLE));
    }
}
