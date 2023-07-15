package acats.fromanotherworld.registry;

import java.util.HashMap;

public class DatapackRegistry {
    public static final HashMap<String, String> COMPAT_DATAPACKS = new HashMap<>();
    public static void register(){
        registerCompatDatapack("alexsmobs", "Assimilated Alex's Mobs");
        registerCompatDatapack("guardvillagers", "Assimilated Guard Villagers");
        registerCompatDatapack("mca", "Assimilated Minecraft Comes Alive");
        registerCompatDatapack("biomemakeover", "Assimilated Biome Makeover");
        registerCompatDatapack("spore", "Spore: Fungal Infection Alliance");
        registerCompatDatapack("sculkhorde", "Sculk Horde Alliance");
    }
    private static void registerCompatDatapack(String modID, String name){
        COMPAT_DATAPACKS.put(modID, name);
    }
}
