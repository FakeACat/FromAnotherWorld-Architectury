package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class EffectConfig extends FALConfig {
    @Override
    protected String name() {
        return "effects";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final FALConfigArrayProperty regenCancelling = new FALConfigArrayProperty(
            "regen_cancelling",
            "Effects that prevent Things from resisting damage and healing.",
            new String[]{
                    "gigeresque:acid",
                    "minecraft:wither",
                    "alexscaves:irradiated"
            }
    );

    public final FALConfigArrayProperty thingImmune = new FALConfigArrayProperty(
            "thing_immune",
            "Effects that cannot be applied to Things.",
            new String[]{
                    "minecraft:poison",
                    "spore:*"
            }
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
                this.regenCancelling,
                this.thingImmune
        };
    }
}
