package acats.fromanotherworld.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;

public class General {

    private static final int CURRENT_VERSION = 3;

    private static final int DEFAULT_SPECIAL_BEHAVIOUR_RARITY = 25;
    public static int specialBehaviourRarity = DEFAULT_SPECIAL_BEHAVIOUR_RARITY;

    private static final boolean DEFAULT_THING_EVENTS_ENABLED = true;
    public static boolean thingEventsEnabled = DEFAULT_THING_EVENTS_ENABLED;

    private static final int DEFAULT_FIRST_EVENT_DAY = 1;
    public static int firstEventDay = DEFAULT_FIRST_EVENT_DAY;

    private static final int DEFAULT_MIN_DAYS_BETWEEN_EVENTS = 30;
    public static int minDaysBetweenEvents = DEFAULT_MIN_DAYS_BETWEEN_EVENTS;

    private static final int DEFAULT_MAX_DAYS_BETWEEN_EVENTS = 40;
    public static int maxDaysBetweenEvents = DEFAULT_MAX_DAYS_BETWEEN_EVENTS;

    private static final boolean AUTO_REGEN_OUTDATED_CONFIGS_DEFAULT = true;
    public static boolean autoRegenOutdatedConfigs = AUTO_REGEN_OUTDATED_CONFIGS_DEFAULT;

    private static final int DEFAULT_MAX_MINIBOSS_TIER = 3;
    public static int maxMinibossTier = DEFAULT_MAX_MINIBOSS_TIER;

    private static File getFile(){
        return new File(Config.getFolder(), "general.json");
    }

    public static void load(){
        if (getFile().exists()){
            autoRegenOutdatedConfigs = setBoolean("autoRegenOutdatedConfigs", AUTO_REGEN_OUTDATED_CONFIGS_DEFAULT);
            if (autoRegenOutdatedConfigs){
                int ver = setInt("version", 0);
                if (ver < CURRENT_VERSION){
                    genFile(false);
                }
            }
        }
        else{
            genFile(true);
        }
        setValues();
    }

    private static void addProperties(JsonObject cfg){
        cfg.addProperty("version", CURRENT_VERSION);
        cfg.addProperty("specialBehaviourRarity", DEFAULT_SPECIAL_BEHAVIOUR_RARITY);
        cfg.addProperty("thingEventsEnabled", DEFAULT_THING_EVENTS_ENABLED);
        cfg.addProperty("firstEventDay", DEFAULT_FIRST_EVENT_DAY);
        cfg.addProperty("minDaysBetweenEvents", DEFAULT_MIN_DAYS_BETWEEN_EVENTS);
        cfg.addProperty("maxDaysBetweenEvents", DEFAULT_MAX_DAYS_BETWEEN_EVENTS);
        cfg.addProperty("autoRegenOutdatedConfigs", AUTO_REGEN_OUTDATED_CONFIGS_DEFAULT);
        cfg.addProperty("maxMinibossTier", DEFAULT_MAX_MINIBOSS_TIER);
    }
    private static void setValues(){
        specialBehaviourRarity = setInt("specialBehaviourRarity", DEFAULT_SPECIAL_BEHAVIOUR_RARITY);
        thingEventsEnabled = setBoolean("thingEventsEnabled", DEFAULT_THING_EVENTS_ENABLED);
        firstEventDay = setInt("firstEventDay", DEFAULT_FIRST_EVENT_DAY);
        minDaysBetweenEvents = setInt("minDaysBetweenEvents", DEFAULT_MIN_DAYS_BETWEEN_EVENTS);
        maxDaysBetweenEvents = setInt("maxDaysBetweenEvents", DEFAULT_MAX_DAYS_BETWEEN_EVENTS);
        maxMinibossTier = setInt("maxMinibossTier", DEFAULT_MAX_MINIBOSS_TIER);
    }

    private static int setInt(String property, int defaultValue){
        try {
            Reader reader = new FileReader(getFile());
            JsonElement jsonElement = new Gson().fromJson(reader, JsonObject.class).get(property);
            if (jsonElement != null){
                return jsonElement.getAsInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private static boolean setBoolean(String property, boolean defaultValue){
        try {
            Reader reader = new FileReader(getFile());
            JsonElement jsonElement = new Gson().fromJson(reader, JsonObject.class).get(property);
            if (jsonElement != null){
                return jsonElement.getAsBoolean();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private static void genFile(boolean create){
        JsonObject cfg = new JsonObject();
        addProperties(cfg);
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
}
