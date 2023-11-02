package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class AssimilatedSculkConfig extends FALConfig {
    @Override
    protected String name() {
        return "assimilated_sculk";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final FALConfigBooleanProperty revealFromMobs = new FALConfigBooleanProperty(
            "reveal_from_detecting_mobs",
            "Should Assimilated Sculk Activators reveal nearby assimilated sculk if they detect a mob?",
            false
    );

    public final FALConfigIntegerProperty tentacleViewRange = new FALConfigIntegerProperty(
            "tentacle_view_range",
            "How far should you be able to see Assimilated Sculk Tentacles from?\nReducing this should help with performance",
            64
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.revealFromMobs,
                this.tentacleViewRange
        };
    }
}
