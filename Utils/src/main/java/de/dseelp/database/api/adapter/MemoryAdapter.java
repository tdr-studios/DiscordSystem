package de.dseelp.database.api.adapter;

import de.dseelp.database.api.DatabaseAdapter;
import de.dseelp.database.api.column.ColumnEntry;
import de.dseelp.database.api.table.Table;
import de.dseelp.database.api.table.TableEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class MemoryAdapter implements DatabaseAdapter {
    private List<Table> tables;
    private HashMap<Table, List<TableEntry>> entries;
    @Override
    public void connect() {
        if (tables == null) tables = new ArrayList<>();
        if (entries == null) entries = new HashMap<>();
    }

    @Override
    public void disconnect() {
        tables.clear();
        entries.clear();
    }

    @Override
    public Table createTable(Table table) {
        tables.add(table);
        entries.put(table, new ArrayList<>());
        return table;
    }

    @Override
    public void deleteTable(Table table) {
        tables.remove(table);
        entries.remove(table);
    }

    @Override
    public Table getTable(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName)) return table;
        }
        return null;
    }

    @Override
    public boolean tableExists(String table) {
        return getTable(table) != null;
    }

    @Override
    public void addEntry(Table table, TableEntry entry) {
        List<TableEntry> entries = this.entries.get(getTable(table.getName()));
        entries.add(entry);
        this.entries.put(getTable(table.getName()), entries);
    }

    @Override
    public void updateEntry(Table table, TableEntry entry) {
        deleteEntry(table, entry);
        addEntry(table, entry);
    }

    @Override
    public void deleteEntry(Table table, TableEntry entry) {
        List<TableEntry> entries = this.entries.get(getTable(table.getName()));
        entries.remove(entry);
        this.entries.put(getTable(table.getName()), entries);
    }

    @Override
    public TableEntry getEntry(Table table, ColumnEntry key) {
        List<TableEntry> entries = this.entries.get(getTable(table.getName()));
        for (TableEntry entry : entries) {
            ColumnEntry primary = entry.getPrimary();
            if (primary.equals(key)) return entry;
        }
        return null;
    }

    @Override
    public TableEntry[] getEntries(Table table) {
        List<TableEntry> entries = this.entries.get(table);
        TableEntry[] array = new TableEntry[entries.size()];
        return entries.toArray(array);
    }

}
