package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class CompatibilityConfig extends FALConfig {
    @Override
    protected String name() {
        return "mod_compat";
    }

    @Override
    protected int version() {
        return 1;
    }

    /*public final FALConfigIntegerProperty sporeCompatMode = new FALConfigIntegerProperty(
            "spore_compat_mode",
            """
                    Compatibility mode for Spore: Fungal Infection
                    0 - Spore mobs are allies of The Thing
                    1 - Spore mobs are attacked by The Thing
                    2 - Spore mobs can be assimilated by The Thing
                    You may need to modify the config of Spore: Fungal Infection for some options to work correctly""",
            0
    );/*

    /*public final FALConfigIntegerProperty sculkCompatMode = new FALConfigIntegerProperty(
            "sculk_compat_mode",
            """
                    Compatibility mode for Sculk Horde
                    0 - Sculk Horde mobs are allies of The Thing
                    1 - Sculk Horde mobs are attacked by The Thing
                    2 - Sculk Horde mobs can be assimilated by The Thing
                    You may need to modify the config of Sculk Horde for some options to work correctly""",
            0
    );*/

    public final FALConfigBooleanProperty fightFungus = new FALConfigBooleanProperty(
            "fight_fungus",
            "Should Things attack mobs from Spore: Fungal Infection?\n" +
                    "You may need to modify the config of Spore: Fungal Infection for them to fight correctly",
            false
    );

    public final FALConfigBooleanProperty fightSculk = new FALConfigBooleanProperty(
            "fight_sculk",
            "Should Things attack mobs from Sculk Horde?\n" +
                    "You may need to modify the config of Sculk Horde for them to fight correctly",
            false
    );

    public final FALConfigBooleanProperty xenoAllies = new FALConfigBooleanProperty(
            "xeno_allies",
            "Gigeresque Xenomorphs do not attack Things",
            true
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                //this.sporeCompatMode,
                //this.sculkCompatMode,
                this.fightFungus,
                this.fightSculk,
                this.xenoAllies
        };
    }
}
