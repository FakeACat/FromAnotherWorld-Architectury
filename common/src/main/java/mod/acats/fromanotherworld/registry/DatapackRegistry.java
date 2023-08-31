package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.DataPackLoader;
import mod.acats.fromanotherworld.config.Config;

public class DatapackRegistry {
    public static void register(DataPackLoader loader){
        loader.addModCompat("alexsmobs");
        loader.addModCompat("guardvillagers");
        loader.addModCompat("mca");
        loader.addModCompat("biomemakeover");
        if (Config.COMPATIBILITY_CONFIG.sporeAllies.get()) {
            loader.addModCompat("spore");
        }
        if (Config.COMPATIBILITY_CONFIG.sculkAllies.get()) {
            loader.addModCompat("sculkhorde");
        }
    }
}
