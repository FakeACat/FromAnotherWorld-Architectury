package acats.fromanotherworld.config;

import acats.fromanotherworld.FromAnotherWorld;

import java.io.File;
import java.nio.file.Path;

public class Config {
    private static Path path;
    public static void load(Path modLoaderSpecificPath){
        path = modLoaderSpecificPath;
        if (!getFolder().exists()){
            getFolder().mkdirs();
        }
        General.load();
        Classification.load();
    }

    public static File getFolder() {
        return new File(path.toFile(), FromAnotherWorld.MOD_ID + "/");
    }
}
