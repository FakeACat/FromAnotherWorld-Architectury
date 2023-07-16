package acats.fromanotherworld.config;

public class GoreConfig extends FAWConfig {
    @Override
    String name() {
        return "gore";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigBooleanProperty enabled = new FAWConfigBooleanProperty(
            "enabled",
            "Should corpses that spread gore be placed when Things are killed?",
            true
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                enabled
        };
    }
}
