package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SpawningConfig extends FALConfig {
    @Override
    protected String name() {
        return "spawning";
    }

    @Override
    protected int version() {
        return 1;
    }

    public final ArrayList<Entry<?>> entries = new ArrayList<>();

    private <T extends Monster> void create(Supplier<EntityType<T>> supplier, String name, int weight, int min, int max){
        create(supplier, name, weight, min, max, true);
    }
    private <T extends Monster> void create(Supplier<EntityType<T>> supplier, String name, int weight, int min, int max, boolean enabledByDefault){
        Entry<T> entry = new Entry<>(supplier, enabledByDefault, name, weight, min, max);
        entries.add(entry);
    }

    public final FALConfigBooleanProperty enabled = new FALConfigBooleanProperty(
            "enabled",
            "Should Things spawn like vanilla mobs?\nNot recommended for the intended experience.",
            false
    );

    public final FALConfigIntegerProperty firstSpawningDay = new FALConfigIntegerProperty(
            "first_spawning_day",
            "The first day that Things can start spawning naturally.\nThis option treats the first day in a world as day 1, unlike the vanilla F3 screen.",
            2
    );

    public final FALConfigFloatProperty failureChance = new FALConfigFloatProperty(
            "failure_chance",
            "The chance for a Thing to fail to spawn even when all the criteria are met.\nCan be used to make Things spawn less than vanilla spawn weight values allow, which is useful considering they do not despawn\n0.0 - Things never fail to spawn\n1.0 - Things always fail to spawn",
            0.95F
    );

    @Override
    protected FALConfigProperty<?>[] properties() {

        create(EntityRegistry.BLOOD_CRAWLER::get, "blood_crawler", 10, 3, 5);

        create(EntityRegistry.CRAWLER::get, "crawler", 5, 1, 3);
        create(EntityRegistry.JULIETTE_THING::get, "juliette_thing", 5, 1, 2);
        create(EntityRegistry.PALMER_THING::get, "palmer_thing", 3, 1, 1);
        create(EntityRegistry.SPLIT_FACE::get, "split_face", 1, 1, 1);
        create(EntityRegistry.BLAIR_THING::get, "blair_thing", 1, 1, 1);

        create(EntityRegistry.DOGBEAST_SPITTER::get, "dogbeast_spitter", 10, 1, 3);
        create(EntityRegistry.DOGBEAST::get, "dogbeast", 1, 8, 24);
        create(EntityRegistry.IMPALER::get, "impaler", 5, 1, 2);
        create(EntityRegistry.PROWLER::get, "prowler", 1, 2, 2);
        create(EntityRegistry.BEAST::get, "beast", 1, 1, 1);

        create(EntityRegistry.ALIEN_THING::get, "alien_thing", 1, 1, 1, false);

        ArrayList<FALConfigProperty<?>> list = Lists.newArrayList(
                this.enabled,
                this.firstSpawningDay,
                this.failureChance
        );
        for (Entry<?> e:
             entries) {
            list.add(e.configProperty);
        }
        return list.toArray(new FALConfigProperty[0]);
    }

    public class Entry<T extends Monster> {
        public final Supplier<EntityType<T>> supplier;
        public final FALConfigSpawnEntryProperty configProperty;
        public Entry(Supplier<EntityType<T>> supplier, boolean enabledByDefault, String name, int weight, int min, int max){
            this.supplier = supplier;
            this.configProperty = new FALConfigSpawnEntryProperty(
                    name,
                    null,
                    enabledByDefault,
                    weight,
                    min,
                    max
            );
        }
    }
}
