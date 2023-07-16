package acats.fromanotherworld.config;

public class EventConfig extends FAWConfig {
    @Override
    String name() {
        return "events";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigBooleanProperty enabled = new FAWConfigBooleanProperty(
            "enabled",
            "Should Thing invasion events happen?",
            true
    );

    public final FAWConfigIntegerProperty firstEventDay = new FAWConfigIntegerProperty(
            "first_event_day",
            "The first day that an invasion event will happen (this option treats the first day in a world as day 1, unlike the vanilla F3 screen).",
            1
    );

    public final FAWConfigIntegerProperty minCooldown = new FAWConfigIntegerProperty(
            "min_cooldown",
            "The minimum number of days between events.",
            30
    );

    public final FAWConfigIntegerProperty maxCooldown = new FAWConfigIntegerProperty(
            "max_cooldown",
            "The maximum number of days between events.",
            40
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.enabled,
                this.firstEventDay,
                this.minCooldown,
                this.maxCooldown
        };
    }
}
