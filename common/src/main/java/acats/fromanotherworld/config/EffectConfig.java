package acats.fromanotherworld.config;

public class EffectConfig extends FAWConfig {
    @Override
    String name() {
        return "effects";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigArrayProperty regenCancelling = new FAWConfigArrayProperty(
            "regen_cancelling",
            "Effects that prevent Things from resisting damage and healing",
            new String[]{
                    "gigeresque:acid",
                    "minecraft:wither"
            }
    );

    public final FAWConfigArrayProperty thingImmune = new FAWConfigArrayProperty(
            "thing_immune",
            "Effects that cannot be applied to Things",
            new String[]{
                    "minecraft:poison",
                    "spore:*"
            }
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.regenCancelling,
                this.thingImmune
        };
    }
}
