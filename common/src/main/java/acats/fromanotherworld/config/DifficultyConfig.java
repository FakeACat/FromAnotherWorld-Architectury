package acats.fromanotherworld.config;

public class DifficultyConfig extends FAWConfig {
    @Override
    String name() {
        return "difficulty";
    }

    @Override
    int version() {
        return 1;
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

    public final FAWConfigIntegerProperty crawlerMergeChance = new FAWConfigIntegerProperty(
            "crawler_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800
    );
    public final FAWConfigIntegerProperty julietteThingMergeChance = new FAWConfigIntegerProperty(
            "juliette_thing_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty palmerThingMergeChance = new FAWConfigIntegerProperty(
            "palmer_thing_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty splitFaceMergeChance = new FAWConfigIntegerProperty(
            "split_face_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty blairThingMergeChance = new FAWConfigIntegerProperty(
            "blair_thing_merge_chance",
            null,
            800
    );

    public final FAWConfigIntegerProperty dogBeastSpitterMergeChance = new FAWConfigIntegerProperty(
            "dogbeast_spitter_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty dogBeastMergeChance = new FAWConfigIntegerProperty(
            "dogbeast_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty impalerMergeChance = new FAWConfigIntegerProperty(
            "impaler_merge_chance",
            null,
            800
    );
    public final FAWConfigIntegerProperty prowlerMergeChance = new FAWConfigIntegerProperty(
            "prowler_merge_chance",
            null,
            1600
    );
    public final FAWConfigIntegerProperty beastMergeChance = new FAWConfigIntegerProperty(
            "beast_merge_chance",
            null,
            800
    );
    public final FAWConfigBooleanProperty burrowing = new FAWConfigBooleanProperty(
            "burrowing",
            "Most Things should be able to burrow if they cannot reach where they are trying to pathfind to.",
            false
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[]{
                this.specialBehaviourRarity,
                this.maxMinibossTier,
                this.crawlerMergeChance,
                this.julietteThingMergeChance,
                this.palmerThingMergeChance,
                this.splitFaceMergeChance,
                this.blairThingMergeChance,
                this.dogBeastSpitterMergeChance,
                this.dogBeastMergeChance,
                this.impalerMergeChance,
                this.prowlerMergeChance,
                this.beastMergeChance,
                this.burrowing
        };
    }
}
