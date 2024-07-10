package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;

public class MiscConfig extends ModConfig {
    @Override
    public String name() {
        return "misc";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final BooleanProperty warnRedistribution = addProperty(new BooleanProperty(
            "warn_redistribution",
            "Should a chat message warning about websites redistributing this mod be posted on joining a world?",
            true,
            false
    ));
}
