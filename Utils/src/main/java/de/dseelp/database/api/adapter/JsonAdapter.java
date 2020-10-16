package de.dseelp.database.api.adapter;

import com.google.gson.*;
import de.dseelp.database.api.DatabaseAdapter;
import de.dseelp.database.api.column.Column;
import de.dseelp.database.api.column.ColumnEntry;
import de.dseelp.database.api.storage.StorageObject;
import de.dseelp.database.api.table.Table;
import de.dseelp.database.api.table.TableEntry;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class JsonAdapter implements DatabaseAdapter {
    private Gson gson;
    private File file;
    private JsonObject object;

    private JsonObject fromColumn(Column column) {
        JsonObject object = new JsonObject();
        object.addProperty("name", column.getName().toLowerCase());
        object.addProperty("primary", column.isPrimary());
        object.addProperty("class", column.getClazz().getName());
        return object;
    }

    private Column toColumn(JsonObject object) {
        Column column = new Column();
        column.setName(object.get("name").getAsString());
        column.setPrimary(object.get("primary").getAsBoolean());
        try {
            column.setClazz(Class.forName(object.get("class").getAsString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return column;
    }

    private JsonObject fromTable(Table table) {
        JsonObject object = new JsonObject();
        JsonArray columns = new JsonArray();
        for (Column column : table.getColumns()) {
            columns.add(fromColumn(column));
        }
        object.addProperty("name", table.getName());
        object.add("columns", columns);
        return object;
    }

    private Table toTable(JsonObject object) {
        String name = object.get("name").getAsString();
        List<Column> columns = new ArrayList<>();
        JsonArray columnArray = object.get("columns").getAsJsonArray();
        for (JsonElement element : columnArray) {
            if (element.isJsonObject()) {
                columns.add(toColumn(element.getAsJsonObject()));
            }else {
                throw new RuntimeException("Invalid JsonTable!");
            }
        }
        return new Table(name, this, columns.toArray(new Column[columns.size()]));
    }

    private JsonObject fromColumnEntry(ColumnEntry entry) {
        JsonObject object = new JsonObject();
        if (checkStorageObject(entry.getValue())) throw new RuntimeException("StorageObject "+ entry.getValue().getClass().getName()+" need a constructer without parameters!");
        object.add("column", fromColumn(entry.getColumn()));
        object.addProperty("value", entry.getValue().getAsString());
        return object;
    }

    private ColumnEntry toColumnEntry(JsonObject object) {
        ColumnEntry entry = new ColumnEntry();
        entry.setColumn(toColumn(object.getAsJsonObject("column")));
        StorageObject storageObject = null;
        try {
            storageObject = (StorageObject) entry.getColumn().getClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        storageObject.load(object.get("value").getAsString());
        entry.setValue(storageObject);
        return entry;
    }

    public boolean checkStorageObject(StorageObject object) {
        for (Constructor<?> constructor : object.getClass().getConstructors()) {
            if (constructor.getParameterCount() != 0) return false;
        }
        return true;
    }

    public JsonAdapter(boolean pretty, File file) {
        this.file = file;
        if (pretty) gson = new GsonBuilder().setPrettyPrinting().create();
        else gson = new GsonBuilder().create();
    }

    private void save() {
        if (object != null) {
            try {
                FileWriter writer = new FileWriter(file, false);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connect() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                object = new JsonObject();
                object.add("tables", new JsonObject());
                object.add("entries", new JsonObject());
                save();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            object = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        object = null;
    }

    public void reload() {
        disconnect();
        connect();
    }

    @Override
    public Table createTable(Table table) {
        if (getTable(table.getName()) == null) {
            JsonObject tables = object.get("tables").getAsJsonObject();
            tables.add(table.getName().toLowerCase(), fromTable(table));
            save();
        }
        return table;
    }

    @Override
    public void deleteTable(Table table) {
        if (getTable(table.getName()) != null) {
            JsonObject tables = object.get("tables").getAsJsonObject();
            tables.remove(table.getName().toLowerCase());
            save();
        }
    }

    @Override
    public Table getTable(String tableName) {
        JsonObject tables = object.get("tables").getAsJsonObject();
        if (tables.has(tableName.toLowerCase())) {
            return toTable(tables.get(tableName.toLowerCase()).getAsJsonObject());
        }
        return null;
    }

    @Override
    public boolean tableExists(String table) {
        return getTable(table) != null;
    }

    @Override
    public void addEntry(Table table, TableEntry entry) {
        Table savedTable = getTable(table.getName());
        if (savedTable == null) throw new RuntimeException("Table not exists!");
        JsonObject entries = object.get("entries").getAsJsonObject();
        if (!entries.has(savedTable.getName().toLowerCase())) {
            JsonObject object = new JsonObject();
            object.add("entries", new JsonArray());
            entries.add(savedTable.getName().toLowerCase(), object);
        }
        JsonArray entryArray = entries.getAsJsonObject(savedTable.getName().toLowerCase()).getAsJsonArray("entries");
        TableEntry saved = getEntry(table, entry.getPrimary());
        if (saved == null) {
            JsonArray array = new JsonArray();
            for (ColumnEntry columnEntry : entry.getEntries()) {
                array.add(fromColumnEntry(columnEntry));
            }
            entryArray.add(array);
        }else {
            JsonArray toModify = null;
            for (JsonElement element : entryArray) {
                JsonArray array = element.getAsJsonArray();
                boolean match = false;
                for (JsonElement jsonElement : array) {
                    JsonObject object = jsonElement.getAsJsonObject();
                    ColumnEntry columnEntry = toColumnEntry(object);
                    if (columnEntry.getColumn().isPrimary()) {
                        if (columnEntry.getValue().compare(entry.getPrimary().getValue())) {
                            match = true;
                        }
                    }
                }
                if (match) {
                    toModify = array;
                    break;
                }
            }
            if (toModify != null) {
                List<JsonElement> toRemove = new ArrayList<>();
                toModify.forEach(toRemove::add);
                for (JsonElement element : toRemove) {
                    toModify.remove(element);
                }
                toRemove = null;
                for (ColumnEntry columnEntry1 : entry.getEntries()) {
                    toModify.add(fromColumnEntry(columnEntry1));
                }
            }
        }

        save();
    }

    @Override
    public void updateEntry(Table table, TableEntry entry) {
        deleteEntry(table, entry);
        addEntry(table,entry);
    }

    @Override
    public void deleteEntry(Table table, TableEntry entry) {
        JsonArray entryArray = object.getAsJsonObject("entries").getAsJsonObject(table.getName().toLowerCase()).getAsJsonArray("entries");
        JsonArray array = new JsonArray();
        for (ColumnEntry columnEntry : entry.getEntries()) {
            array.add(fromColumnEntry(columnEntry));
        }
        entryArray.remove(array);
        save();
    }

    @Override
    public TableEntry getEntry(Table table, ColumnEntry key) {
        if (!key.getColumn().isPrimary()) return null;
        StorageObject keyObject = key.getValue();
        for (TableEntry entry : getEntries(table)) {
            StorageObject primary = entry.getPrimary().getValue();
            boolean compared = primary.compare(keyObject);
            if (compared) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public TableEntry[] getEntries(Table table) {
        JsonObject entries = object.get("entries").getAsJsonObject();
        if (!entries.has(table.getName().toLowerCase())) {
            JsonObject object = new JsonObject();
            object.add("entries", new JsonArray());
            entries.add(table.getName().toLowerCase(), object);
        }
        List<TableEntry> tableEntries = new ArrayList<>();
        JsonArray parentArray = entries.getAsJsonObject(table.getName().toLowerCase()).getAsJsonArray("entries");
        for (JsonElement element : parentArray) {
            JsonArray entryArray = element.getAsJsonArray();
            TableEntry entry = new TableEntry(new ArrayList<>());
            for (JsonElement jsonElement : entryArray) {
                JsonObject object = jsonElement.getAsJsonObject();
                entry.getEntries().add(toColumnEntry(object));
            }
            tableEntries.add(entry);
        }
        return tableEntries.toArray(new TableEntry[tableEntries.size()]);
    }
}
