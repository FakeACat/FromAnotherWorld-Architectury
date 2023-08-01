package acats.fromanotherworld.config;

import acats.fromanotherworld.constants.TimeInTicks;

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
            "1 in this number chance for any wall tentacle block with sufficient support to instead be a wall palmer.",
            10
    );

    public final FAWConfigIntegerProperty tunnelGoreTime = new FAWConfigIntegerProperty(
            "tunnel_gore_time",
            "Minimum age of a tunnel block in ticks required for it to start spreading gore blocks.\nSet to negative to disable.",
            TimeInTicks.HOUR
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.enabled,
                this.wallPalmerChance,
                this.tunnelGoreTime
        };
    }
}
