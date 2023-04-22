package acats.fromanotherworld.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.Objects;

public class Classification {

    private static final int CURRENT_VERSION = 3;

    private static final String[] REGEN_CANCELLING_DEBUFFS = {
            "gigeresque:acid",
            "minecraft:wither"
    };
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

        regenCancellingDebuffs = arrayFromString("RegenCancellingDebuffs");
    }

    private static void genFile(boolean create){
        JsonObject cfg = new JsonObject();
        cfg.addProperty("version", CURRENT_VERSION);
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
