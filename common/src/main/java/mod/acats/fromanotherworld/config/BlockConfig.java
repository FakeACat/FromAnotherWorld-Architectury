package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;
import mod.acats.fromanotherworld.constants.TimeInTicks;

public class BlockConfig extends ModConfig {
    @Override
    public String name() {
        return "block";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final BooleanProperty corpsesEnabled = addProperty(new BooleanProperty(
            "corpses_enabled",
            "Should corpses that spread gore be placed when Things are killed?",
            true,
            false
    ));

    public final BooleanProperty sprawlingTentaclesEnabled = addProperty(new BooleanProperty(
            "sprawling_tentacles_enabled",
            "Disabling does not remove existing sprawling tentacles, however it does stop them from spreading",
            true,
            false
    ));

    public final IntegerProperty sprawlingTentaclesSpreadChance = addProperty(new IntegerProperty(
            "sprawling_tentacles_spread_chance",
            "1 in this number chance for sprawling tentacles to attempt to spread every random tick",
            6,
            false
    ));

    public final BooleanProperty disguisedTendrilsEnabled = addProperty(new BooleanProperty(
            "disguised_tendrils_enabled",
            "Disabling does not remove existing disguised tendrils, however it does stop them from spreading",
            false,
            false
    ));

    public final IntegerProperty disguisedTendrilsSpreadChance = addProperty(new IntegerProperty(
            "disguised_tendrils_spread_chance",
            "1 in this number chance for disguised tendrils to attempt to spread every random tick",
            6,
            false
    ));

    public final BooleanProperty tunnelsEnabled = addProperty(new BooleanProperty(
            "tunnels_enabled",
            "Should Things be allowed to place tunnels?\n" +
                    "Disabling does not remove existing tunnels or prevent Things from using them, however no more will be placed",
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
