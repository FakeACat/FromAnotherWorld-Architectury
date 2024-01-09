package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;
import mod.acats.fromanotherworld.constants.TimeInTicks;

public class GoreConfig extends ModConfig {
    @Override
    public String name() {
        return "gore";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final BooleanProperty enabled = addProperty(new BooleanProperty(
            "enabled",
            "Should corpses that spread gore be placed when Things are killed?",
            true,
            false
    ));

    public final IntegerProperty wallPalmerChance = addProperty(new IntegerProperty(
            "wall_palmer_chance",
            "1 in this number chance for any wall tentacle block with sufficient support to instead be a wall palmer.",
            10,
            false
    ));

    public final IntegerProperty tunnelGoreTime = addProperty(new IntegerProperty(
            "tunnel_gore_time",
            "Minimum age of a tunnel block in ticks required for it to start spreading gore blocks.\nSet to a negative value to disable.",
            TimeInTicks.HOUR,
            false
    ));
}
