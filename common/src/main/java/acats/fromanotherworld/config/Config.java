package acats.fromanotherworld.config;

import acats.fromanotherworld.FromAnotherWorld;

import java.io.File;
import java.nio.file.Path;

public class Config {
    private static Path path;

    public static final GoreConfig goreConfig = new GoreConfig();
    public static final EventConfig eventConfig = new EventConfig();
    public static final DifficultyConfig difficultyConfig = new DifficultyConfig();
    public static final EffectConfig effectConfig = new EffectConfig();
    public static final SpawningConfig spawningConfig = new SpawningConfig();

    public static void load(Path modLoaderSpecificPath){
        path = modLoaderSpecificPath;
        if (!getFolder().exists()){
            getFolder().mkdirs();
        }
        goreConfig.load();
        eventConfig.load();
        difficultyConfig.load();
        effectConfig.load();
        spawningConfig.load();
    }

    public static File getFolder() {
        return new File(path.toFile(), FromAnotherWorld.MOD_ID + "/");
    }
}
