package acats.fromanotherworld.constants;

import mod.azure.azurelib.core.animation.RawAnimation;

public final class FAWAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
    public static final RawAnimation FREEZING = RawAnimation.begin().thenPlayXTimes("misc.freezing", 1).thenPlayAndHold("misc.frozen");
    public static final RawAnimation FROZEN = RawAnimation.begin().thenPlayAndHold("misc.frozen");

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("move.swim");
    public static final RawAnimation CHASE = RawAnimation.begin().thenLoop("move.chase");
    public static final RawAnimation BURROW = RawAnimation.begin().thenPlayAndHold("move.burrow");
    public static final RawAnimation EMERGE = RawAnimation.begin().thenPlayAndHold("move.emerge");
}
