package acats.fromanotherworld.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.Objects;

public class Classification {

    private static final int CURRENT_VERSION = 2;

    private static final String[] HUMANOIDS = {
            "minecraft:villager",
            "minecraft:wandering_trader",
            "minecraft:piglin",
            "minecraft:evoker",
            "minecraft:piglin_brute",
            "minecraft:pillager",
            "minecraft:vindicator",
            "minecraft:witch",
            "mca:male_villager",
            "mca:female_villager",
            "guardvillagers:guard"
    };

    private static final String[] LARGE_QUADRUPEDS = {
            "minecraft:hoglin",
            "minecraft:panda",
            "minecraft:polar_bear",
            "minecraft:ravager",
            "minecraft:sniffer"
    };

    private static final String[] QUADRUPEDS = {
            "minecraft:cat",
            "minecraft:cow",
            "minecraft:donkey",
            "minecraft:fox",
            "minecraft:horse",
            "minecraft:llama",
            "minecraft:mooshroom",
            "minecraft:mule",
            "minecraft:ocelot",
            "minecraft:pig",
            "minecraft:sheep",
            "minecraft:turtle",
            "minecraft:wolf",
            "minecraft:trader_llama",
            "minecraft:goat",
            "minecraft:camel"
    };

    private static final String[] SMALL = {
            "minecraft:axolotl",
            "minecraft:chicken",
            "minecraft:frog",
            "minecraft:parrot",
            "minecraft:rabbit",
            "minecraft:silverfish",
            "minecraft:tadpole"
    };

    private static final String[] ATTACKABLE_BUT_NOT_ASSIMILABLE = {
            "minecraft:iron_golem",
            "minecraft:snow_golem",
            "minecraft:wither",
            "minecraft:warden",
            "minecraft:allay"
    };

    private static final String[] REGEN_CANCELLING_DEBUFFS = {
            "gigeresque:acid"
    };
    private static String[] humanoids = HUMANOIDS;
    private static String[] largeQuadrupeds = LARGE_QUADRUPEDS;
    private static String[] quadrupeds = QUADRUPEDS;
    private static String[] small = SMALL;
    private static String[] attackableButNotAssimilable = ATTACKABLE_BUT_NOT_ASSIMILABLE;
    private static String[] regenCancellingDebuffs = REGEN_CANCELLING_DEBUFFS;

    private static File getFile(){
        return new File(Config.getFolder(), "classification.json");
    }

    public static void load(){
        if (getFile().exists()){
            if (General.autoRegenOutdatedConfigs){
                int ver = 0;
                try {
                    Reader reader = new FileReader(getFile());
                    JsonElement jsonElement = new Gson().fromJson(reader, JsonObject.class).get("version");
                    if (jsonElement != null){
                        ver = jsonElement.getAsInt();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (ver < CURRENT_VERSION){
                    genFile(false);
                }
            }
        }
        else {
            genFile(true);
        }

        humanoids = arrayFromString("Humanoids");
        largeQuadrupeds = arrayFromString("LargeQuadrupeds");
        quadrupeds = arrayFromString("Quadrupeds");
        small = arrayFromString("Small");
        attackableButNotAssimilable = arrayFromString("AttackableButNotAssimilable");
        regenCancellingDebuffs = arrayFromString("RegenCancellingDebuffs");
    }

    private static void genFile(boolean create){
        JsonObject cfg = new JsonObject();
        cfg.addProperty("version", CURRENT_VERSION);
        cfg.addProperty("Humanoids", createString(HUMANOIDS));
        cfg.addProperty("LargeQuadrupeds", createString(LARGE_QUADRUPEDS));
        cfg.addProperty("Quadrupeds", createString(QUADRUPEDS));
        cfg.addProperty("Small", createString(SMALL));
        cfg.addProperty("AttackableButNotAssimilable", createString(ATTACKABLE_BUT_NOT_ASSIMILABLE));
        cfg.addProperty("RegenCancellingDebuffs", createString(REGEN_CANCELLING_DEBUFFS));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (create){
            try {
                getFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            fileWriter.write(gson.toJson(cfg));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createString(String[] stringArray){
        if (stringArray == null)
            return "null";

        int iMax = stringArray.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(stringArray[i]);
            if (i == iMax)
                return b.toString();
            b.append(", ");
        }
    }

    private static boolean isX(Entity entity, String[] category){
        for (String s:
                category) {
            if (Objects.equals(s, EntityType.getId(entity.getType()).toString())){
                return true;
            }
        }
        return false;
    }

    private static String[] arrayFromString(String string){
        try {
            Reader reader = new FileReader(getFile());
            JsonElement jsonElement = new Gson().fromJson(reader, JsonObject.class).get(string);
            if (jsonElement != null)
                return jsonElement.getAsString().replace(" ", "").split(",");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static boolean isHumanoid(Entity entity){
        return isX(entity, humanoids);
    }

    public static boolean isLargeQuadruped(Entity entity){
        return isX(entity, largeQuadrupeds);
    }

    public static boolean isQuadruped(Entity entity){
        return isX(entity, quadrupeds);
    }

    public static boolean isSmall(Entity entity){
        return isX(entity, small);
    }

    public static boolean isAttackableButNotAssimilable(Entity entity){
        return isX(entity, attackableButNotAssimilable) || (entity instanceof PlayerEntity p && !p.isCreative() && !p.isSpectator());
    }

    public static boolean isRegenPreventative(StatusEffectInstance statusEffectInstance){
        Identifier id = Registries.STATUS_EFFECT.getId(statusEffectInstance.getEffectType());
        if (id != null){
            for (String s:
                    regenCancellingDebuffs) {
                if (Objects.equals(id.toString(), s)){
                    return true;
                }
            }
        }
        return false;
    }
}
