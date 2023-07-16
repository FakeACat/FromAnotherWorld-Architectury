package acats.fromanotherworld.config;

public class DifficultyConfig extends FAWConfig {
    @Override
    String name() {
        return "difficulty";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigIntegerProperty specialBehaviourRarity = new FAWConfigIntegerProperty(
            "special_behaviour_rarity",
            "1 in this number chance for Things to get each special ability.",
            25
    );

    public final FAWConfigIntegerProperty maxMinibossTier = new FAWConfigIntegerProperty(
            "max_miniboss_tier",
            "Maximum tier that minibosses can grow to. They start at tier 0.",
            3
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.specialBehaviourRarity,
                this.maxMinibossTier
        };
    }
}
