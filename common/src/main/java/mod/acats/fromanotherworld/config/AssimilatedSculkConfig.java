package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class AssimilatedSculkConfig extends FALConfig {
    @Override
    protected String name() {
        return "assimilated_sculk";
    }

    @Override
    protected int version() {
        return 2;
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

    public final FALConfigIntegerProperty alienSculkPlacement = new FALConfigIntegerProperty(
            "alien_assimilated_sculk_placement",
            "1 in this number chance every 3 seconds for the Alien Thing to attempt to place an Assimilated Sculk Tentacle while in the 1951 form.\n" +
                    "Values less than 1 disable this feature.",
            20
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.revealFromMobs,
                this.tentacleViewRange,
                this.alienSculkPlacement
        };
    }
}
