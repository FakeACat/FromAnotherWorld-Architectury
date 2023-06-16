package acats.fromanotherworld.registry;

import java.util.HashMap;

public class DatapackRegistry {
    public static final HashMap<String, String> COMPAT_DATAPACKS = new HashMap<>();
    public static void register(){
        registerCompatDatapack("alexsmobs", "Alex's Mobs");
        registerCompatDatapack("guardvillagers", "Guard Villagers");
        registerCompatDatapack("mca", "Minecraft Comes Alive");
        registerCompatDatapack("biomemakeover", "Biome Makeover");
    }
    private static void registerCompatDatapack(String modID, String name){
        COMPAT_DATAPACKS.put(modID, name);
    }
}
