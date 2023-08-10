package mod.acats.fromanotherworld.config;

public class CompatibilityConfig extends FAWConfig {
    @Override
    String name() {
        return "mod_compat";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigBooleanProperty sporeAllies = new FAWConfigBooleanProperty(
            "spore_allies",
            "Things never fight Fungal Infection mobs?",
            true
    );

    public final FAWConfigBooleanProperty sculkAllies = new FAWConfigBooleanProperty(
            "sculk_allies",
            "Things never fight Sculk Horde mobs?",
            true
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.sporeAllies,
                this.sculkAllies
        };
    }
}
