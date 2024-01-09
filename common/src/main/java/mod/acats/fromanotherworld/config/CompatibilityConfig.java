package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;

public class CompatibilityConfig extends ModConfig {
    @Override
    public String name() {
        return "mod_compat";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final BooleanProperty fightFungus = addProperty(new BooleanProperty(
            "fight_fungus",
            "Should Things attack mobs from Fungal Infection: Spore?\n" +
                    "You may need to modify the config of Fungal Infection: Spore for them to fight correctly",
            false,
            true
    ));

    public final BooleanProperty fightSculk = addProperty(new BooleanProperty(
            "fight_sculk",
            "Should Things attack mobs from Sculk Horde?\n" +
                    "You may need to modify the config of Sculk Horde for them to fight correctly",
            false,
            true
    ));

    public final BooleanProperty xenoAllies = addProperty(new BooleanProperty(
            "xeno_allies",
            "Gigeresque Xenomorphs do not attack Things",
            true,
            true
    ));
}
