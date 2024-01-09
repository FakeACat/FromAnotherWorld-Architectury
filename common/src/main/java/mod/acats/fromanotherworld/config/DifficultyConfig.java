package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.FloatProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;

public class DifficultyConfig extends ModConfig {
    @Override
    public String name() {
        return "difficulty";
    }

    @Override
    protected int version() {
        return 2;
    }

    public final IntegerProperty specialBehaviourRarity = addProperty(new IntegerProperty(
            "special_behaviour_rarity",
            "1 in this number chance for Things to get each special ability.",
            25,
            false
    ));

    public final IntegerProperty maxMinibossTier = addProperty(new IntegerProperty(
            "max_miniboss_tier",
            "Maximum tier that minibosses can grow to. They start at tier 0.",
            3,
            false
    ));

    public final IntegerProperty crawlerMergeChance = addProperty(new IntegerProperty(
            "crawler_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty julietteThingMergeChance = addProperty(new IntegerProperty(
            "juliette_thing_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty palmerThingMergeChance = addProperty(new IntegerProperty(
            "palmer_thing_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty splitFaceMergeChance = addProperty(new IntegerProperty(
            "split_face_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty blairThingMergeChance = addProperty(new IntegerProperty(
            "blair_thing_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty dogBeastSpitterMergeChance = addProperty(new IntegerProperty(
            "dogbeast_spitter_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty dogBeastMergeChance = addProperty(new IntegerProperty(
            "dogbeast_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty impalerMergeChance = addProperty(new IntegerProperty(
            "impaler_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));

    public final IntegerProperty prowlerMergeChance = addProperty(new IntegerProperty(
            "prowler_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            1600,
            false
    ));
    public final IntegerProperty beastMergeChance = addProperty(new IntegerProperty(
            "beast_merge_chance",
            "1 in this number chance every tick to attempt to merge.",
            800,
            false
    ));
    public final BooleanProperty burrowing = addProperty(new BooleanProperty(
            "burrowing",
            "Most Things should be able to burrow if they cannot reach where they are trying to pathfind to.",
            false,
            false
    ));
    public final FloatProperty maxGriefingHardness = addProperty(new FloatProperty(
            "max_griefing_hardness",
            "The maximum hardness that Things can break. Set to a negative value to disable all Thing block breaking.\nBlocks with negative hardness values can never be broken.\nTo blacklist specific blocks, add to the thing_immune tag using a data pack.",
            49.0F,
            false
    ));

    private FloatProperty createDamageMultiplierOption(String category, String examples, float defaultValue) {
        return addProperty(new FloatProperty(
                category + "_damage_multiplier",
                "The damage multiplier for " + category + " tier Things such as " + examples + " when not vulnerable.\n\n" +
                        "0.0 means they will take no damage, 0.5 means they will take half damage, 1.0 means they will take full damage.",
                defaultValue,
                false
        ));
    }

    public final FloatProperty revealedDamageMultiplier = createDamageMultiplierOption(
            "revealed",
            "Chest Spitters and Vine Tentacles",
            0.25F
    );

    public final FloatProperty fodderDamageMultiplier = createDamageMultiplierOption(
            "fodder",
            "Blood Crawlers",
            1.0F
    );

    public final FloatProperty splitDamageMultiplier = createDamageMultiplierOption(
            "split",
            "Crawlers and DogBeast Spitters",
            0.2F
    );

    public final FloatProperty standardDamageMultiplier = createDamageMultiplierOption(
            "standard",
            "Juliette Things and DogBeasts",
            0.2F
    );

    public final FloatProperty eliteDamageMultiplier = createDamageMultiplierOption(
            "elite",
            "Palmer Things and Impalers",
            0.2F
    );

    public final FloatProperty mergedDamageMultiplier = createDamageMultiplierOption(
            "merged",
            "Split Faces and Prowlers",
            0.1F
    );

    public final FloatProperty minibossDamageMultiplier = createDamageMultiplierOption(
            "miniboss",
            "Blair Things and Beasts",
            0.05F
    );

    public final FloatProperty specialMinibossDamageMultiplier = createDamageMultiplierOption(
            "special_miniboss",
            "Alien Things",
            0.05F
    );
}
