package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherworld.config.Config;

import java.util.HashMap;

public class DatapackRegistry {
    public static final HashMap<String, String> COMPAT_DATAPACKS = new HashMap<>();
    public static void register(){
        registerCompatDatapack("alexsmobs", "Assimilated Alex's Mobs");
        registerCompatDatapack("guardvillagers", "Assimilated Guard Villagers");
        registerCompatDatapack("mca", "Assimilated Minecraft Comes Alive");
        registerCompatDatapack("biomemakeover", "Assimilated Biome Makeover");
        if (Config.COMPATIBILITY_CONFIG.sporeAllies.get()) {
            registerCompatDatapack("spore", "Spore: Fungal Infection Alliance");
        }
        if (Config.COMPATIBILITY_CONFIG.sculkAllies.get()) {
            registerCompatDatapack("sculkhorde", "Sculk Horde Alliance");
        }
    }
    private static void registerCompatDatapack(String modID, String name){
        COMPAT_DATAPACKS.put(modID, name);
    }
}
