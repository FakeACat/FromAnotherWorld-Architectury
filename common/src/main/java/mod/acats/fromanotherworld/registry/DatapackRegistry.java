package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.platform.ModLoaderSpecific;
import mod.acats.fromanotherlibrary.registry.DataPackLoader;
import mod.acats.fromanotherworld.config.Config;

public class DatapackRegistry {
    public static void register(DataPackLoader loader){
        loader.addModCompat("alexsmobs");
        loader.addModCompat("guardvillagers");
        loader.addModCompat("mca");
        loader.addModCompat("biomemakeover");

        if (ModLoaderSpecific.INSTANCE.isModLoaded("spore")){
            /*switch (Config.COMPATIBILITY_CONFIG.sporeCompatMode.get()) {
                case 0 -> loader.addDataPack("spore_alliance");
                case 1 -> loader.addDataPack("spore_combat");
                case 2 -> loader.addDataPack("spore_assimilation");
            }*/

            if (Config.COMPATIBILITY_CONFIG.fightFungus.get()) {
                loader.addDataPack("spore_combat");
            }
            else {
                loader.addDataPack("spore_alliance");
            }
        }
        if (ModLoaderSpecific.INSTANCE.isModLoaded("sculkhorde")) {
            /*switch (Config.COMPATIBILITY_CONFIG.sculkCompatMode.get()) {
                case 0 -> loader.addDataPack("sculkhorde_alliance");
                case 1 -> loader.addDataPack("sculkhorde_combat");
                case 2 -> loader.addDataPack("sculkhorde_assimilation");
            }*/

            if (Config.COMPATIBILITY_CONFIG.fightSculk.get()) {
                loader.addDataPack("sculkhorde_combat");
            }
            else {
                loader.addDataPack("sculkhorde_alliance");
            }
        }
        if (ModLoaderSpecific.INSTANCE.isModLoaded("gigeresque") && Config.COMPATIBILITY_CONFIG.xenoAllies.get()) {
            loader.addDataPack("gigeresque_alliance");
        }
    }
}
