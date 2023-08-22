package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class CompatibilityConfig extends FALConfig {
    @Override
    protected String name() {
        return "mod_compat";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final FALConfigBooleanProperty sporeAllies = new FALConfigBooleanProperty(
            "spore_allies",
            "Things never fight Fungal Infection mobs?",
            true
    );

    public final FALConfigBooleanProperty sculkAllies = new FALConfigBooleanProperty(
            "sculk_allies",
            "Things never fight Sculk Horde mobs?",
            true
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.sporeAllies,
                this.sculkAllies
        };
    }
}
