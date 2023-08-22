package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class EventConfig extends FALConfig {
    @Override
    protected String name() {
        return "events";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final FALConfigBooleanProperty enabled = new FALConfigBooleanProperty(
            "enabled",
            "Should Thing invasion events happen?",
            true
    );

    public final FALConfigIntegerProperty firstEventDay = new FALConfigIntegerProperty(
            "first_event_day",
            "The first day that an invasion event will happen.\nThis option treats the first day in a world as day 1, unlike the vanilla F3 screen.",
            1
    );

    public final FALConfigIntegerProperty minCooldown = new FALConfigIntegerProperty(
            "min_cooldown",
            "The minimum number of days between events.",
            30
    );

    public final FALConfigIntegerProperty maxCooldown = new FALConfigIntegerProperty(
            "max_cooldown",
            "The maximum number of days between events.",
            40
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.enabled,
                this.firstEventDay,
                this.minCooldown,
                this.maxCooldown
        };
    }
}
