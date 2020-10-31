package de.tdrstudios.discordsystem.utils;

import com.google.gson.*;

import java.util.*;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class JsonDocument {

    public JsonDocument(JsonObject object) {
        this.object = object;
    }

    public JsonDocument() {
        this.object = new JsonObject();
    }

    public JsonObject getObject() {
        return object;
    }

    @Override
    public String toString() {
        return GsonUtils.get().toJson(object);
    }

    public String toPrettyString() {
        return GsonUtils.getPretty().toJson(object);
    }

    private JsonObject object;

    public void add(String key, String value) {
        getElementSetter(key).addProperty(getLastKey(key), value);
    }

    public void add(String key, Number value) {
        getElementSetter(key).addProperty(getLastKey(key), value);
    }

    public void add(String key, Boolean value) {
        getElementSetter(key).addProperty(getLastKey(key), value);
    }

    public void add(String key, Character value) {
        getElementSetter(key).addProperty(getLastKey(key), value);
    }

    public void addStringList(String key, Collection<String> list) {
        JsonArray array = new JsonArray();
        for (String string : list) {
            array.add(new JsonPrimitive(string));
        }
        getElementSetter(key).add(getLastKey(key), array);
    }

    public void addUUIDList(String key, Collection<UUID> list) {
        List<String> strings = new ArrayList<>();
        for (UUID uuid : list) {
            strings.add(uuid.toString());
        }
        addStringList(key, strings);
    }

    public void addNumberList(String key, Collection<Number> list) {
        JsonArray array = new JsonArray();
        for (Number number : list) {
            array.add(new JsonPrimitive(number));
        }
        getElementSetter(key).add(getLastKey(key), array);
    }

    public void addCharacterList(String key, Collection<Character> list) {
        JsonArray array = new JsonArray();
        for (Character character : list) {
            array.add(new JsonPrimitive(character));
        }
        getElementSetter(key).add(getLastKey(key), array);
    }

    public void addElement(String key, JsonElement element) {
        getElementSetter(key).add(getLastKey(key), element);
    }

    public void addDocument(String key, JsonDocument document) {
        getElementSetter(key).add(getLastKey(key), document.getObject());
    }

    public String getString(String key) {
        return getElement(key).getAsString();
    }

    public Number getNumber(String key) {
        return getElement(key).getAsNumber();
    }

    public int getInt(String key) {
        return getElement(key).getAsInt();
    }

    public double getDouble(String key) {
        return getElement(key).getAsDouble();
    }

    public long getLong(String key) {
        return getElement(key).getAsLong();
    }

    public float getFloat(String key) {
        return getElement(key).getAsFloat();
    }

    public boolean getBoolean(String key) {
        return getElement(key).getAsBoolean();
    }

    public char getCharacter(String key) {
        return getElement(key).getAsJsonPrimitive().getAsCharacter();
    }

    public Collection<String> getStringList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<String> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsString());
                    return list;
                }
            }
        }
        return null;
    }

    public Collection<UUID> getUUIDList(String key) {
        Collection<String> strings = getStringList(key);
        List<UUID> uuids = new ArrayList<>();
        for (String string : strings) {
            uuids.add(UUID.fromString(string));
        }
        return uuids;
    }

    public Collection<Number> getNumberList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Number> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsNumber());
                    return list;
                }
            }
        }
        return null;
    }

    public Collection<Integer> getIntList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Integer> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsInt());
                    return list;
                }
            }
        }
        return null;
    }

    public Collection<Long> getLongList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Long> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsLong());
                    return list;
                }
            }
        }
        return null;
    }

    public List<Double> getDoubleList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Double> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsDouble());
                    return list;
                }
            }
        }
        return null;
    }

    public List<Float> getFloatList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Float> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsFloat());
                    return list;
                }
            }
        }
        return null;
    }

    public Collection<Character> getCharList(String key) {
        JsonElement element = getElement(key);
        if (element.isJsonArray()) {
            List<Character> list = new ArrayList<>();
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) return Collections.emptyList();
            for (JsonElement jsonElement : array) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsJsonPrimitive().getAsCharacter());
                    return list;
                }
            }
        }
        return null;
    }

    public boolean has(String key) {
        JsonElement element = getElement(key);
        if (element instanceof JsonNull) return false;
        return element != null;
    }

    public JsonObject getObject(String key) {
        return getElement(key).getAsJsonObject();
    }

    public JsonDocument getDocument(String key) {
        return new JsonDocument(getObject(key));
    }

    public JsonDocument deepCopy() {
        return new JsonDocument(object.deepCopy());
    }

    public Set<String> keySet() {
        return object.keySet();
    }

    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return object.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonDocument that = (JsonDocument) o;

        return this.getObject().equals(that.getObject());
    }

    private JsonElement getElement(String path){

        String[] parts = path.split("\\.");
        JsonElement result = object;

        for (String key : parts) {

            key = key.trim();
            if (key.isEmpty())
                continue;

            if (result == null){
                result = JsonNull.INSTANCE;
                break;
            }

            if (result.isJsonObject()){
                result = ((JsonObject)result).get(key);
            }
            else if (result.isJsonArray()){
                int ix = Integer.valueOf(key) - 1;
                result = ((JsonArray)result).get(ix);
            }
            else break;
        }

        return result;
    }

    private JsonObject getElementSetter(String path){

        String[] parts = path.split("\\.");
        JsonElement old = object;
        JsonElement result = object;

        if (parts.length == 1) return object;

        for (int i = 0; i < parts.length; i++) {
            String key = parts[i];
            if (parts.length > 1){
                if (i == parts.length-1) break;
            }
            key = key.trim();
            if (key.isEmpty())
                continue;

            if (result == null){
                result = new JsonObject();
                if (old != null )old.getAsJsonObject().add(key, result);
            }

            if (result.isJsonObject()){
                old = result;
                result = ((JsonObject)result).get(key);
                if (result == null){
                    result = new JsonObject();
                    if (old != null )old.getAsJsonObject().add(key, result);
                }
            }
            else if (result.isJsonArray()){
                int ix = Integer.valueOf(key) - 1;
                result = ((JsonArray)result).get(ix);
            }
            else break;
        }
        return result.getAsJsonObject();
    }

    private String getLastKey(String s) {
        if (s.contains(".")) {
            String[] split = s.split("\\.");
            if (split.length > 0) {
                return split[split.length-1];
            }else return s;
        }else return s;
    }
}
