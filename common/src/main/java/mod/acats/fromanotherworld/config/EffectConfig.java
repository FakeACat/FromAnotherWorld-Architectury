package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.ArrayProperty;

public class EffectConfig extends ModConfig {
    @Override
    public String name() {
        return "effects";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final ArrayProperty regenCancelling = addProperty(new ArrayProperty(
            "regen_cancelling",
            "Effects that prevent Things from resisting damage and healing.",
            new String[]{
                    "gigeresque:acid",
                    "minecraft:wither",
                    "alexscaves:irradiated"
            },
            false
    ));

    public final ArrayProperty thingImmune = addProperty(new ArrayProperty(
            "thing_immune",
            "Effects that cannot be applied to Things.",
            new String[]{
                    "minecraft:poison",
                    "spore:*"
            },
            false
    ));
}
