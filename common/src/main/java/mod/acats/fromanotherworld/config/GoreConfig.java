package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;
import mod.acats.fromanotherworld.constants.TimeInTicks;

public class GoreConfig extends FALConfig {
    @Override
    protected String name() {
        return "gore";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final FALConfigBooleanProperty enabled = new FALConfigBooleanProperty(
            "enabled",
            "Should corpses that spread gore be placed when Things are killed?",
            true
    );

    public final FALConfigIntegerProperty wallPalmerChance = new FALConfigIntegerProperty(
            "wall_palmer_chance",
            "1 in this number chance for any wall tentacle block with sufficient support to instead be a wall palmer.",
            10
    );

    public final FALConfigIntegerProperty tunnelGoreTime = new FALConfigIntegerProperty(
            "tunnel_gore_time",
            "Minimum age of a tunnel block in ticks required for it to start spreading gore blocks.\nSet to a negative value to disable.",
            TimeInTicks.HOUR
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.enabled,
                this.wallPalmerChance,
                this.tunnelGoreTime
        };
    }
}
