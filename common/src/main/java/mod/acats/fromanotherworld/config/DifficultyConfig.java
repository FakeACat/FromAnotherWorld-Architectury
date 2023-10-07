package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class DifficultyConfig extends FALConfig {
    @Override
    protected String name() {
        return "difficulty";
    }

    @Override
    protected int version() {
        return 2;
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
            "max_griefing_hardness",
            "The maximum hardness that Things can break. Set to a negative value to disable all Thing block breaking.\nBlocks with negative hardness values can never be broken.\nTo blacklist specific blocks, add to the thing_immune tag using a data pack.",
            49.0F
    );

    private FALConfigFloatProperty createDamageMultiplierOption(String category, String examples, float defaultValue) {
        return new FALConfigFloatProperty(
                category + "_damage_multiplier",
                "The damage multiplier for " + category + " tier Things such as " + examples + " when not vulnerable\n" +
                        "0.0 means they will take no damage, 0.5 means they will take half damage, 1.0 means they will take full damage",
                defaultValue
        );
    }

    public final FALConfigFloatProperty revealedDamageMultiplier = this.createDamageMultiplierOption(
            "revealed",
            "Chest Spitters and Vine Tentacles",
            0.25F
    );

    public final FALConfigFloatProperty fodderDamageMultiplier = this.createDamageMultiplierOption(
            "fodder",
            "Blood Crawlers",
            1.0F
    );

    public final FALConfigFloatProperty splitDamageMultiplier = this.createDamageMultiplierOption(
            "split",
            "Crawlers and DogBeast Spitters",
            0.2F
    );

    public final FALConfigFloatProperty standardDamageMultiplier = this.createDamageMultiplierOption(
            "standard",
            "Juliette Things and DogBeasts",
            0.2F
    );

    public final FALConfigFloatProperty eliteDamageMultiplier = this.createDamageMultiplierOption(
            "elite",
            "Palmer Things and Impalers",
            0.2F
    );

    public final FALConfigFloatProperty mergedDamageMultiplier = this.createDamageMultiplierOption(
            "merged",
            "Split Faces and Prowlers",
            0.1F
    );

    public final FALConfigFloatProperty minibossDamageMultiplier = this.createDamageMultiplierOption(
            "miniboss",
            "Blair Things and Beasts",
            0.05F
    );

    public final FALConfigFloatProperty specialMinibossDamageMultiplier = this.createDamageMultiplierOption(
            "special_miniboss",
            "Alien Things",
            0.05F
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
                this.maxGriefingHardness,
                this.revealedDamageMultiplier,
                this.fodderDamageMultiplier,
                this.splitDamageMultiplier,
                this.standardDamageMultiplier,
                this.eliteDamageMultiplier,
                this.mergedDamageMultiplier,
                this.minibossDamageMultiplier,
                this.specialMinibossDamageMultiplier
        };
    }
}
