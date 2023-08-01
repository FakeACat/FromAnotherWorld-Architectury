package acats.fromanotherworld.config;

import acats.fromanotherworld.FromAnotherWorld;
import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class FAWConfig {
    private static final int GLOBAL_VERSION = 1;
    abstract String name();
    abstract int version();
    abstract FAWConfigProperty<?>[] properties();

    private int actualVersion() {
        return this.version() + GLOBAL_VERSION;
    }

    private final FAWConfigBooleanProperty autoRegenOutdated = new FAWConfigBooleanProperty(
            "auto_regen_outdated",
            "Should this file automatically regenerate whenever the default options are changed?",
            true);
    private final FAWConfigIntegerProperty version = new FAWConfigIntegerProperty(
            "version",
            "Config version number used for " + autoRegenOutdated.getName() + ". Do not modify.",
            this.actualVersion());

    public void load(){
        if (this.getFile().exists()){
            this.autoRegenOutdated.set();
            if (this.autoRegenOutdated.get()){
                this.version.set();
                if (this.version.get() < this.actualVersion()){
                    this.version.set(this.actualVersion());
                    this.genFile(false);
                }
            }
        }
        else{
            this.genFile(true);
        }
        this.setValues();
    }

    private void addProperties(JsonObject object) {
        addProperty(object, version);
        addProperty(object, autoRegenOutdated);

        for (FAWConfigProperty<?> property:
             this.properties()) {
            addProperty(object, property);
        }
    }

    private static void addProperty(JsonObject object, FAWConfigProperty<?> property) {
        JsonObject obj = new JsonObject();
        if (property.description != null) {
            String[] descriptionLines = property.description.split("\n");
            for (int i = 0; i < descriptionLines.length; i++) {
                String numer = descriptionLines.length > 1 ? String.valueOf(i + 1) : "";
                obj.addProperty("description" + numer, descriptionLines[i]);
            }
        }
        property.addTo(obj);
        object.add(property.getName(), obj);
    }

    private void setValues() {
        for (FAWConfigProperty<?> property:
                this.properties()) {
            property.set();
        }
    }

    private File getFile(){
        return new File(Config.getFolder(), this.name() + ".json");
    }

    private void genFile(boolean create) {
        JsonObject cfg = new JsonObject();
        this.addProperties(cfg);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (create){
            try {
                if (!getFile().createNewFile()) {
                    FromAnotherWorld.LOGGER.error("Unable to create config file " + this.name());
                }
            } catch (IOException e) {
                FromAnotherWorld.LOGGER.error("IOException while attempting to generate config file " + this.name() + ": " + e.getMessage());
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            fileWriter.write(gson.toJson(cfg));
            fileWriter.close();
        } catch (IOException e) {
            FromAnotherWorld.LOGGER.error("Failed writing to config file " + this.name() + ": " + e.getMessage());
        }
    }

    abstract class FAWConfigProperty<T> {
        private final String name;
        private final @Nullable String description;
        private T value;

        private FAWConfigProperty(String name, @Nullable String description, T defaultValue) {
            this.name = name;
            this.description = description;
            this.value = defaultValue;
        }

        public T get(){
            return this.value;
        }

        void set(){
            try {
                Reader reader = new FileReader(getFile());
                JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class).getAsJsonObject(this.name);
                if (jsonObject != null){
                    this.value = this.getFrom(jsonObject);
                }
            } catch (FileNotFoundException e) {
                FromAnotherWorld.LOGGER.error("FileNotFoundException while trying to read " + this.getName() + " from config file " + name() + ": " + e.getMessage());
            }
        }

        void set(T value){
            this.value = value;
        }

        public String getName(){
            return this.name;
        }

        abstract T getFrom(JsonObject object);

        abstract void addTo(JsonObject object);
    }

    public class FAWConfigIntegerProperty extends FAWConfigProperty<Integer> {

        FAWConfigIntegerProperty(String name, @Nullable String description, Integer defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        Integer getFrom(JsonObject object) {
            return object.get("value").getAsInt();
        }

        @Override
        void addTo(JsonObject object) {
            object.addProperty("value", this.get());
        }
    }

    public class FAWConfigBooleanProperty extends FAWConfigProperty<Boolean> {

        FAWConfigBooleanProperty(String name, @Nullable String description, Boolean defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        Boolean getFrom(JsonObject object) {
            return object.get("value").getAsBoolean();
        }

        @Override
        void addTo(JsonObject object) {
            object.addProperty("value", this.get());
        }
    }

    public class FAWConfigArrayProperty extends FAWConfigProperty<String[]> {

        public FAWConfigArrayProperty(String name, @Nullable String description, String[] defaultValue) {
            super(name, description, defaultValue);
        }

        private final ArrayList<String> mods = new ArrayList<>();

        private static JsonArray toJsonArray(String[] array){
            JsonArray jsonArray = new JsonArray();
            for (String string:
                    array) {
                jsonArray.add(string);
            }
            return jsonArray;
        }

        @Override
        String[] getFrom(JsonObject object) {
            JsonArray jsonArray = object.get("value").getAsJsonArray();
            ArrayList<String> strings = new ArrayList<>();
            for (JsonElement e:
                 jsonArray) {
                String s = e.getAsString();
                if (s.endsWith(":*")){
                    mods.add(s.split(":")[0]);
                }
                else{
                    strings.add(s);
                }
            }
            return strings.toArray(new String[0]);
        }

        @Override
        void addTo(JsonObject object) {
            object.add("value", toJsonArray(this.get()));
        }

        public boolean contains(String s){
            return Arrays.asList(this.get()).contains(s) || this.mods.contains(s.split(":")[0]);
        }
    }

    public class FAWConfigSpawnEntryProperty extends FAWConfigProperty<Boolean> {

        public FAWConfigSpawnEntryProperty(String name, @Nullable String description, boolean defaultEnabled, int defaultWeight, int defaultMin, int defaultMax) {
            super(name, description, defaultEnabled);
            this.weight = defaultWeight;
            this.min = defaultMin;
            this.max = defaultMax;
        }

        private int weight;
        private int min;
        private int max;

        public int getWeight() {
            return this.weight;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        @Override
        Boolean getFrom(JsonObject object) {
            this.weight = object.get("spawn_weight").getAsInt();
            this.min = object.get("minimum_group_size").getAsInt();
            this.max = object.get("maximum_group_size").getAsInt();
            return object.get("enabled").getAsBoolean();
        }

        @Override
        void addTo(JsonObject object) {
            object.addProperty("enabled", this.get());
            object.addProperty("spawn_weight", this.getWeight());
            object.addProperty("minimum_group_size", this.getMin());
            object.addProperty("maximum_group_size", this.getMax());
        }
    }
}
