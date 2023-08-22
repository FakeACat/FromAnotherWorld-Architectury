package mod.acats.fromanotherworld.config;

import java.io.File;

public class Config {
    public static final GoreConfig GORE_CONFIG = new GoreConfig();
    public static final EventConfig EVENT_CONFIG = new EventConfig();
    public static final DifficultyConfig DIFFICULTY_CONFIG = new DifficultyConfig();
    public static final EffectConfig EFFECT_CONFIG = new EffectConfig();
    public static final SpawningConfig SPAWNING_CONFIG = new SpawningConfig();
    public static final WorldConfig WORLD_CONFIG = new WorldConfig();
    public static final CompatibilityConfig COMPATIBILITY_CONFIG = new CompatibilityConfig();

    public static void load(File folder){
        GORE_CONFIG.load(folder);
        EVENT_CONFIG.load(folder);
        DIFFICULTY_CONFIG.load(folder);
        EFFECT_CONFIG.load(folder);
        SPAWNING_CONFIG.load(folder);
        WORLD_CONFIG.load(folder);
        COMPATIBILITY_CONFIG.load(folder);
    }
}
