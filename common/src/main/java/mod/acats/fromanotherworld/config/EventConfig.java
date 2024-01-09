package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;

public class EventConfig extends ModConfig {
    @Override
    public String name() {
        return "events";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final BooleanProperty enabled = addProperty(new BooleanProperty(
            "enabled",
            "Should Thing invasion events happen?",
            true,
            false
    ));

    public final IntegerProperty firstEventDay = addProperty(new IntegerProperty(
            "first_event_day",
            "The first day that an invasion event will happen.\nThis option treats the first day in a world as day 1, unlike the vanilla F3 screen.",
            1,
            false
    ));

    public final IntegerProperty minCooldown = addProperty(new IntegerProperty(
            "min_cooldown",
            "The minimum number of days between events.",
            30,
            false
    ));

    public final IntegerProperty maxCooldown = addProperty(new IntegerProperty(
            "max_cooldown",
            "The maximum number of days between events.",
            40,
            false
    ));
}
