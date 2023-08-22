package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class DifficultyConfig extends FALConfig {
    @Override
    protected String name() {
        return "difficulty";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final FALConfigIntegerProperty specialBehaviourRarity = new FALConfigIntegerProperty(
            "special_behaviour_rarity",
            "1 in this number chance for Things to get each special ability.",
            25
    );

    public final FALConfigIntegerProperty maxMinibossTier = new FALConfigIntegerProperty(
            "max_miniboss_tier",
            "Maximum tier that minibosses can grow to. They start at tier 0.",
            3
    );

    public final FALConfigIntegerProperty crawlerMergeChance = new FALConfigIntegerProperty(
            "crawler_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800
    );
    public final FALConfigIntegerProperty julietteThingMergeChance = new FALConfigIntegerProperty(
            "juliette_thing_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty palmerThingMergeChance = new FALConfigIntegerProperty(
            "palmer_thing_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty splitFaceMergeChance = new FALConfigIntegerProperty(
            "split_face_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty blairThingMergeChance = new FALConfigIntegerProperty(
            "blair_thing_merge_chance",
            null,
            800
    );

    public final FALConfigIntegerProperty dogBeastSpitterMergeChance = new FALConfigIntegerProperty(
            "dogbeast_spitter_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty dogBeastMergeChance = new FALConfigIntegerProperty(
            "dogbeast_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty impalerMergeChance = new FALConfigIntegerProperty(
            "impaler_merge_chance",
            null,
            800
    );
    public final FALConfigIntegerProperty prowlerMergeChance = new FALConfigIntegerProperty(
            "prowler_merge_chance",
            null,
            1600
    );
    public final FALConfigIntegerProperty beastMergeChance = new FALConfigIntegerProperty(
            "beast_merge_chance",
            null,
            800
    );
    public final FALConfigBooleanProperty burrowing = new FALConfigBooleanProperty(
            "burrowing",
            "Most Things should be able to burrow if they cannot reach where they are trying to pathfind to.",
            false
    );
    public final FALConfigFloatProperty maxGriefingHardness = new FALConfigFloatProperty(
            "maxGriefingHardness",
            "The maximum hardness that Things can break. Set to a negative value to disable all Thing block breaking.\nBlocks with negative hardness values can never be broken.\nTo blacklist specific blocks, add to the thing_immune tag using a data pack.",
            49.0F
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[]{
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
                this.burrowing,
                this.maxGriefingHardness
        };
    }
}
