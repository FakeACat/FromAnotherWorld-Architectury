package acats.fromanotherworld.config;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class FAWConfig {
    abstract String name();
    abstract int version();
    abstract FAWConfigProperty<?>[] properties();

    private final FAWConfigBooleanProperty autoRegenOutdated = new FAWConfigBooleanProperty(
            "auto_regen_outdated",
            "Should this file automatically regenerate whenever the default options are changed?",
            true);
    private final FAWConfigIntegerProperty version = new FAWConfigIntegerProperty(
            "version",
            "Config version number used for " + autoRegenOutdated.getName() + ". Do not modify.",
            this.version());

    public void load(){
        if (this.getFile().exists()){
            this.autoRegenOutdated.set();
            if (this.autoRegenOutdated.get()){
                this.version.set();
                if (this.version.get() < this.version()){
                    this.version.set(this.version());
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
        addDescription(object, property);
        property.addTo(object);
    }

    private static void addDescription(JsonObject object, FAWConfigProperty<?> property) {
        object.addProperty(property.getName() + "_description", property.description);
    }

    private void setValues() {
        for (FAWConfigProperty<?> property:
                this.properties()) {
            property.set();
        }
    }

    private File getFile(){
        return new File(Config.getFolder(), name() + ".json");
    }

    private void genFile(boolean create) {
        JsonObject cfg = new JsonObject();
        this.addProperties(cfg);
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

    abstract class FAWConfigProperty<T> {
        private final String name;
        private final String description;
        private T value;

        private FAWConfigProperty(String name, String description, T defaultValue) {
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
                JsonElement jsonElement = new Gson().fromJson(reader, JsonObject.class).get(this.name);
                if (jsonElement != null){
                    this.value = this.getFrom(jsonElement);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        void set(T value){
            this.value = value;
        }

        public String getName(){
            return this.name;
        }

        abstract T getFrom(JsonElement element);

        abstract void addTo(JsonObject object);
    }

    public class FAWConfigIntegerProperty extends FAWConfigProperty<Integer> {

        FAWConfigIntegerProperty(String name, String description, Integer defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        Integer getFrom(JsonElement element) {
            return element.getAsInt();
        }

        @Override
        void addTo(JsonObject object) {
            object.addProperty(this.getName(), this.get());
        }
    }

    public class FAWConfigBooleanProperty extends FAWConfigProperty<Boolean> {

        FAWConfigBooleanProperty(String name, String description, Boolean defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        Boolean getFrom(JsonElement element) {
            return element.getAsBoolean();
        }

        @Override
        void addTo(JsonObject object) {
            object.addProperty(this.getName(), this.get());
        }
    }

    public class FAWConfigArrayProperty extends FAWConfigProperty<String[]> {

        public FAWConfigArrayProperty(String name, String description, String[] defaultValue) {
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
        String[] getFrom(JsonElement element) {
            JsonArray jsonArray = element.getAsJsonArray();
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
            object.add(this.getName(), toJsonArray(this.get()));
        }

        public boolean contains(String s){
            return Arrays.asList(this.get()).contains(s) || this.mods.contains(s.split(":")[0]);
        }
    }
}
