package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.SpawnValues;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.FloatProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.IntegerProperty;
import mod.acats.fromanotherlibrary.config.v2.properties.SpawnValuesProperty;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpawningConfig extends ModConfig {

    public SpawningConfig() {

        create(EntityRegistry.BLOOD_CRAWLER, "blood_crawler", 10, 3, 5);

        create(EntityRegistry.CRAWLER, "crawler", 5, 1, 3);
        create(EntityRegistry.JULIETTE_THING, "juliette_thing", 5, 1, 2);
        create(EntityRegistry.PALMER_THING, "palmer_thing", 3, 1, 1);
        create(EntityRegistry.SPLIT_FACE, "split_face", 1, 1, 1);
        create(EntityRegistry.BLAIR_THING, "blair_thing", 1, 1, 1);

        create(EntityRegistry.DOGBEAST_SPITTER, "dogbeast_spitter", 10, 1, 3);
        create(EntityRegistry.DOGBEAST, "dogbeast", 1, 8, 24);
        create(EntityRegistry.IMPALER, "impaler", 5, 1, 2);
        create(EntityRegistry.PROWLER, "prowler", 1, 2, 2);
        create(EntityRegistry.BEAST, "beast", 1, 1, 1);

        create(EntityRegistry.ALIEN_THING, "alien_thing", 1, 1, 1, false);
    }

    @Override
    public String name() {
        return "spawning";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final BooleanProperty enabled = addProperty(new BooleanProperty(
            "enabled",
            "Should Things spawn like vanilla mobs?\nNot recommended for the intended experience.",
            false,
            true
    ));

    public final IntegerProperty firstSpawningDay = addProperty(new IntegerProperty(
            "first_spawning_day",
            "The first day that Things can start spawning naturally.\nThis option treats the first day in a world as day 1, unlike the vanilla F3 screen.",
            2,
            false
    ));

    public final FloatProperty failureChance = addProperty(new FloatProperty(
            "failure_chance",
            "The chance for a Thing to fail to spawn even when all the criteria are met.\nCan be used to make Things spawn less than vanilla spawn weight values allow, which is useful considering they do not despawn\n0.0 - Things never fail to spawn\n1.0 - Things always fail to spawn",
            0.95F,
            false
    ));

    private <T extends Monster> void create(Supplier<EntityType<T>> supplier, String name, int weight, int min, int max){
        create(supplier, name, weight, min, max, true);
    }
    private <T extends Monster> void create(Supplier<EntityType<T>> supplier, String name, int weight, int min, int max, boolean enabledByDefault){
        SpawnValues spawnValues = new SpawnValues(enabledByDefault, weight, min, max);
        SpawnValuesProperty spawnValuesProperty = new SpawnValuesProperty(name, "Mob spawn options", spawnValues);
        spawnEntries.add(new SpawnEntry<>(supplier, addProperty(spawnValuesProperty)));
    }

    public final List<SpawnEntry<?>> spawnEntries = new ArrayList<>();

    public record SpawnEntry<T extends Monster>(Supplier<EntityType<T>> typeSupplier, SpawnValuesProperty values) {
    }
}
