package de.tdrstudios.discordsystem.utils;

import com.google.gson.*;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class JsonConfig {
    private final JsonDocument setted = new JsonDocument();
    private final JsonDocument loaded;
    private final JsonDocument defaults = new JsonDocument();
    private JsonDocument calculated;
    private final File file;

    private JsonConfig(JsonDocument loaded, File file) {
        this.loaded = loaded;
        this.file = file;
    }

    private JsonConfig(File file) {
        this.file = file;
        this.loaded = new JsonDocument();
    }

    private JsonConfig() {
        this.file = null;
        this.loaded = new JsonDocument();
    }

    public static JsonConfig load(File file) {
            try {
                return new JsonConfig(new JsonDocument(JsonParser.parseReader(new FileReader(file)).getAsJsonObject()), file);
            } catch (JsonIOException | JsonSyntaxException | IllegalStateException e) {
                return new JsonConfig(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return null;
    }

    private void calculate() {
        calculated = defaults.deepCopy();
        for (Map.Entry<String, JsonElement> entry : loaded.entrySet()) {
            calculated.addElement(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, JsonElement> entry : setted.entrySet()) {
            calculated.addElement(entry.getKey(), entry.getValue());
        }
    }

    public JsonObject getObject() {
        return calculated.getObject();
    }

    @Override
    public String toString() {
        return calculated.toString();
    }

    public String toPrettyString() {
        return calculated.toPrettyString();
    }

    public void addDefault(String key, String value) {
        defaults.add(key, value);
        calculate();
    }

    public void addDefault(String key, Number value) {
        defaults.add(key, value);
        calculate();
    }

    public void addDefault(String key, Boolean value) {
        defaults.add(key, value);
        calculate();
    }

    public void addDefault(String key, Character value) {
        defaults.add(key, value);
        calculate();
    }

    public void addDefaultStringList(String key, Collection<String> list) {
        defaults.addStringList(key, list);
        calculate();
    }

    public void addDefaultUUIDList(String key, Collection<UUID> list) {
        defaults.addUUIDList(key, list);
        calculate();
    }

    public void addDefaultNumberList(String key, Collection<Number> list) {
        defaults.addNumberList(key, list);
        calculate();
    }

    public void addDefaultCharacterList(String key, Collection<Character> list) {
        defaults.addCharacterList(key, list);
        calculate();
    }

    public void addDefault(String key, JsonElement element) {
        defaults.addElement(key, element);
        calculate();
    }

    public void addDefault(String key, JsonDocument document) {
        defaults.addDocument(key, document);
        calculate();
    }

    public void set(String key, String value) {
        setted.add(key, value);
        calculate();
    }

    public void set(String key, Number value) {
        setted.add(key, value);
        calculate();
    }

    public void set(String key, Boolean value) {
        setted.add(key, value);
        calculate();
    }

    public void set(String key, Character value) {
        setted.add(key, value);
        calculate();
    }

    public void setStringList(String key, Collection<String> list) {
        setted.addStringList(key, list);
        calculate();
    }

    public void setUUIDList(String key, Collection<UUID> list) {
        setted.addUUIDList(key, list);
        calculate();
    }

    public void setNumberList(String key, Collection<Number> list) {
        setted.addNumberList(key, list);
        calculate();
    }

    public void setCharacterList(String key, Collection<Character> list) {
        setted.addCharacterList(key, list);
        calculate();
    }

    public void set(String key, JsonElement element) {
        setted.addElement(key, element);
        calculate();
    }

    public void set(String key, JsonDocument document) {
        setted.addDocument(key, document);
        calculate();
    }

    public String getString(String key) {
        return calculated.getString(key);
    }

    public Number getNumber(String key) {
        return calculated.getNumber(key);
    }

    public int getInt(String key) {
        return calculated.getInt(key);
    }

    public double getDouble(String key) {
        return calculated.getDouble(key);
    }

    public long getLong(String key) {
        return calculated.getLong(key);
    }

    public float getFloat(String key) {
        return calculated.getFloat(key);
    }

    public boolean getBoolean(String key) {
        return calculated.getBoolean(key);
    }

    public char getCharacter(String key) {
        return calculated.getCharacter(key);
    }

    public Collection<String> getStringList(String key) {
        return calculated.getStringList(key);
    }

    public Collection<UUID> getUUIDList(String key) {
        return calculated.getUUIDList(key);
    }

    public Collection<Number> getNumberList(String key) {
        return calculated.getNumberList(key);
    }

    public Collection<Integer> getIntList(String key) {
        return calculated.getIntList(key);
    }

    public Collection<Long> getLongList(String key) {
        return calculated.getLongList(key);
    }

    public List<Double> getDoubleList(String key) {
        return calculated.getDoubleList(key);
    }

    public List<Float> getFloatList(String key) {
        return calculated.getFloatList(key);
    }

    public Collection<Character> getCharList(String key) {
        return calculated.getCharList(key);
    }

    public boolean has(String key) {
        return calculated.has(key);
    }

    public JsonObject getObject(String key) {
        return calculated.getObject(key);
    }

    public JsonDocument getDocument(String key) {
        return calculated.getDocument(key);
    }

    public void save(File file) throws IOException {
        calculate();
        if (calculated.equals(loaded)) return;
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(GsonUtils.getPretty().toJson(calculated.getObject()));
        fileWriter.flush();
        fileWriter.close();
    }

    public void save() throws IOException {
        if (file == null) throw new UnsupportedOperationException("This Config was not initialized with a file please use JsonConfig#save(File)!");
        save(file);
    }
}
