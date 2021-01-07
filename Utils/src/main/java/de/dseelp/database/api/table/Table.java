package de.dseelp.database.api.table;

import de.dseelp.database.api.DatabaseAdapter;
import de.dseelp.database.api.column.Column;
import de.dseelp.database.api.column.ColumnEntry;
import de.dseelp.database.api.storage.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class Table {
    private DatabaseAdapter adapter;
    private final String name;

    public Table(String name, DatabaseAdapter adapter, Column[] columns) {
        this.name = name;
        boolean primary = false;
        for (Column column : columns) {
            if (column.isPrimary()) {
                if (primary) throw new RuntimeException("There can only one primary Column!");
                primary = true;
                this.primary = column;
            }
        }
        if (!primary) throw new RuntimeException("No Primary Column defined!");
        this.columns = columns;
        this.adapter = adapter;
    }

    private Column[] columns;
    private Column primary;

    public Column getPrimary() {
        for (Column column : columns) {
            if (column.isPrimary()) return column;
        }
        return null;
    }

    public void addEntry(StorageObject... objects) {
        if (objects.length == columns.length) {
            List<ColumnEntry> columnEntries = new ArrayList<>();
            for(int i = 0; i < objects.length; ++i) {
                if (objects[i].getClass() != columns[i].getClazz()) {
                    throw new RuntimeException("Invalid class match!");
                }else {
                    columnEntries.add(new ColumnEntry(columns[i], objects[i]));
                }
            }
            adapter.addEntry(this, new TableEntry(columnEntries));
        }
    }

    public void updateEntry(StorageObject... objects) {
        if (objects.length == columns.length) {
            List<ColumnEntry> columnEntries = new ArrayList<>();
            for(int i = 0; i < objects.length; ++i) {
                if (objects[i].getClass() != columns[i].getClazz()) {
                    throw new RuntimeException("Invalid class match!");
                }else {
                    columnEntries.add(new ColumnEntry(columns[i], objects[i]));
                }
            }
            adapter.updateEntry(this, new TableEntry(columnEntries));
        }
    }

    public void deleteEntry(TableEntry entry) {
        adapter.deleteEntry(this, entry);
    }

    public void deleteEntry(ColumnEntry entry) {
        deleteEntry(getEntry(entry));
    }

    public void deleteEntry(String column, String key) {
        deleteEntry(new ColumnEntry(getColumn(column), new StringStorage(key)));
    }

    public void deleteEntry(String column, boolean key) {
        deleteEntry(new ColumnEntry(getColumn(column), new BooleanStorage(key)));
    }

    public void deleteEntry(String column, int key) {
        deleteEntry(new ColumnEntry(getColumn(column), new IntStorage(key)));
    }

    public void deleteEntry(String column, double key) {
        deleteEntry(new ColumnEntry(getColumn(column), new DoubleStorage(key)));
    }

    public void deleteEntry(String column, long key) {
        deleteEntry(new ColumnEntry(getColumn(column), new LongStorage(key)));
    }

    public TableEntry getEntry(ColumnEntry key) {
        return adapter.getEntry(this, key);
    }

    public TableEntry getEntry(String column, String key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), new StringStorage(key)));
    }

    public TableEntry getEntry(String column, boolean key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), new BooleanStorage(key)));
    }

    public TableEntry getEntry(String column, int key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), new IntStorage(key)));
    }

    public TableEntry getEntry(String column, double key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), new DoubleStorage(key)));
    }

    public TableEntry getEntry(String column, long key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), new LongStorage(key)));
    }

    public TableEntry getEntry(String column, StorageObject key) {
        return adapter.getEntry(this, new ColumnEntry(getColumn(column), key));
    }

    public TableEntry getEntry(StorageObject key) {
        return adapter.getEntry(this, new ColumnEntry(primary, key));
    }

    public Column getPrimaryColum() {
        return primary;
    }

    public Column getColumn(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName)) return column;
        }
        return null;
    }

    public DatabaseAdapter getAdapter() {
        return this.adapter;
    }

    public String getName() {
        return this.name;
    }

    public Column[] getColumns() {
        return this.columns;
    }
}
