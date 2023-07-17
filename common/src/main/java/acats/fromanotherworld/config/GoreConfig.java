package acats.fromanotherworld.config;

public class GoreConfig extends FAWConfig {
    @Override
    String name() {
        return "gore";
    }

    @Override
    int version() {
        return 1;
    }

    public final FAWConfigBooleanProperty enabled = new FAWConfigBooleanProperty(
            "enabled",
            "Should corpses that spread gore be placed when Things are killed?",
            true
    );

    public final FAWConfigIntegerProperty wallPalmerChance = new FAWConfigIntegerProperty(
            "wall_palmer_chance",
            "1 in this number chance for any wall tentacle block with sufficient support to instead be a wall palmer",
            10
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.enabled,
                this.wallPalmerChance
        };
    }
}
