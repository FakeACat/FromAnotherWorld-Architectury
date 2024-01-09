package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;

public class AssimilatedSculkConfig extends ModConfig {
    @Override
    public String name() {
        return "assimilated_sculk";
    }

    @Override
    protected int version() {
        return 2;
    }

    public final BooleanProperty revealFromMobs = addProperty(new BooleanProperty(
            "reveal_from_detecting_mobs",
            "Should Assimilated Sculk Activators reveal nearby assimilated sculk if they detect a mob?",
            false,
            false
    ));

    public final IntegerProperty tentacleViewRange = addProperty(new IntegerProperty(
            "tentacle_view_range",
            "How far should you be able to see Assimilated Sculk Tentacles from?\nReducing this should help with performance",
            64,
            false
    ));

    public final IntegerProperty alienSculkPlacement = addProperty(new IntegerProperty(
            "alien_assimilated_sculk_placement",
            "1 in this number chance every 3 seconds for the Alien Thing to attempt to place an Assimilated Sculk Tentacle while in the 1951 form.\n" +
                    "Values less than 1 disable this feature.",
            20,
            false
    ));
}
