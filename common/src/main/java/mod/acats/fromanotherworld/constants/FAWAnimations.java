package mod.acats.fromanotherworld.constants;

import mod.acats.fromanotherworld.entity.interfaces.Leaper;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.azure.azurelib.animatable.GeoBlockEntity;
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
    @Deprecated
    public static final RawAnimation SPIT = RawAnimation.begin().thenPlayAndHold("misc.spit");
    public static final RawAnimation ALWAYS_PLAYING = RawAnimation.begin().thenLoop("misc.always_playing");
    public static final RawAnimation HEAD_IDLE = RawAnimation.begin().thenLoop("misc.head_idle");

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("move.swim");
    public static final RawAnimation CHASE = RawAnimation.begin().thenLoop("move.chase");
    public static final RawAnimation BURROW = RawAnimation.begin().thenPlayAndHold("move.burrow");
    public static final RawAnimation EMERGE = RawAnimation.begin().thenPlayAndHold("move.emerge");
    public static final RawAnimation LEAP = RawAnimation.begin().thenPlayAndHold("move.leap");

    public static final RawAnimation OPEN_MOUTH = RawAnimation.begin().thenPlayAndHold("attack.open_mouth");
    public static final RawAnimation CLOSE_MOUTH = RawAnimation.begin().thenPlayAndHold("attack.close_mouth");
    public static final RawAnimation MELEE = RawAnimation.begin().thenLoop("attack.melee");
    public static final RawAnimation OPEN_MOUTH_THEN_MELEE = RawAnimation.begin().thenPlayXTimes("attack.open_mouth", 1).thenLoop("attack.melee");

    private static <E extends Thing> void frozen(AnimationState<E> event, E thing){
        if (thing.getCold() == 1.0F){
            event.getController().setAnimation(FROZEN);
        }
        else{
            event.getController().setAnimation(FREEZING);
        }
    }

    private static <E extends Thing> void movement(AnimationState<E> event, E thing){
        if (thing.isAggressive()){
            event.getController().setAnimation(CHASE);
        }
        else{
            event.getController().setAnimation(WALK);
        }
    }

    private static <E extends Thing> void generic(AnimationState<E> event,
                                                  E thing,
                                                  BiConsumer<AnimationState<E>, E> frozen,
                                                  BiConsumer<AnimationState<E>, E> movement,
                                                  BiConsumer<AnimationState<E>, E> idle){

        event.getController().setAnimationSpeed(thing.animationSpeed(event));
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

    private static <E extends Thing & Leaper> void genericWithLeap(AnimationState<E> state,
                                                                   E thing,
                                                                   BiConsumer<AnimationState<E>, E> frozen,
                                                                   BiConsumer<AnimationState<E>, E> movement,
                                                                   BiConsumer<AnimationState<E>, E> idle){
        if (thing.isLeaping()){
            state.getController().setAnimation(LEAP);
        }
        else{
            generic(state, thing, frozen, movement, idle);
        }
    }

    public static <E extends Thing> AnimationController<E> genericThing(E thing,
                                                                        String name,
                                                                        BiConsumer<AnimationState<E>, E> frozen,
                                                                        BiConsumer<AnimationState<E>, E> movement,
                                                                        BiConsumer<AnimationState<E>, E> idle){
        return new AnimationController<>(thing, name, 5, (event) -> {
            generic(event, thing, frozen, movement, idle);
            return PlayState.CONTINUE;
        });
    }

    public static <E extends Thing> AnimationController<E> defaultThing(E thing){
        return genericThing(thing,
                "DefaultThing",
                FAWAnimations::frozen,
                FAWAnimations::movement,
                (event2, thing2) -> event2.getController().setAnimation(IDLE));
    }

    public static <E extends Thing> AnimationController<E> defaultThingNoChase(E thing){
        return genericThing(thing,
                "DefaultThingNoChase",
                FAWAnimations::frozen,
                (event2, thing2) -> event2.getController().setAnimation(WALK),
                (event2, thing2) -> event2.getController().setAnimation(IDLE));
    }

    public static <E extends Thing> AnimationController<E> alwaysPlaying(E thing){
        return new AnimationController<>(thing, "AlwaysPlaying", 0, (event) -> {
            if (thing.isThingFrozen()){
                return PlayState.STOP;
            }
            event.getController().setAnimation(ALWAYS_PLAYING);
            return PlayState.CONTINUE;
        });
    }

    public static <E extends Thing & Leaper> AnimationController<E> genericThingWithLeap(E thing,
                                                                        String name,
                                                                        BiConsumer<AnimationState<E>, E> frozen,
                                                                        BiConsumer<AnimationState<E>, E> movement,
                                                                        BiConsumer<AnimationState<E>, E> idle){
        return new AnimationController<>(thing, name, 5, (event) -> {
            genericWithLeap(event, thing, frozen, movement, idle);
            return PlayState.CONTINUE;
        });
    }

    public static <E extends Thing & Leaper> AnimationController<E> defaultThingNoChaseWithLeap(E thing){
        return genericThingWithLeap(thing,
                "DefaultThingNoChaseWithLeap",
                FAWAnimations::frozen,
                (event2, thing2) -> event2.getController().setAnimation(WALK),
                (event2, thing2) -> event2.getController().setAnimation(IDLE));
    }

    public static <E extends GeoBlockEntity> AnimationController<E> blockAlwaysPlaying(E blockEntity){
        return new AnimationController<>(blockEntity, "BlockAlwaysPlaying", 0, (event) -> {
            event.getController().setAnimation(ALWAYS_PLAYING);
            return PlayState.CONTINUE;
        });
    }
}
