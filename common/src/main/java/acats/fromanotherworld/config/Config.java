package acats.fromanotherworld.config;

import acats.fromanotherworld.FromAnotherWorld;

import java.io.File;
import java.nio.file.Path;

public class Config {
    private static Path path;

    public static final GoreConfig GORE_CONFIG = new GoreConfig();
    public static final EventConfig EVENT_CONFIG = new EventConfig();
    public static final DifficultyConfig DIFFICULTY_CONFIG = new DifficultyConfig();
    public static final EffectConfig EFFECT_CONFIG = new EffectConfig();
    public static final SpawningConfig SPAWNING_CONFIG = new SpawningConfig();
    public static final WorldConfig WORLD_CONFIG = new WorldConfig();
    public static final CompatibilityConfig COMPATIBILITY_CONFIG = new CompatibilityConfig();

    public static void load(Path modLoaderSpecificPath){
        path = modLoaderSpecificPath;
        if (!getFolder().exists()){
            getFolder().mkdirs();
        }
        GORE_CONFIG.load();
        EVENT_CONFIG.load();
        DIFFICULTY_CONFIG.load();
        EFFECT_CONFIG.load();
        SPAWNING_CONFIG.load();
        WORLD_CONFIG.load();
        COMPATIBILITY_CONFIG.load();
    }

    public static File getFolder() {
        return new File(path.toFile(), FromAnotherWorld.MOD_ID + "/");
    }
}
