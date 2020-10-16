package de.dseelp.database.api;

import de.dseelp.database.api.column.Column;
import de.dseelp.database.api.table.Table;
import de.dseelp.database.api.table.TableEntry;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class Database {
    private DatabaseAdapter adapter;

    public static Database createDatabase(DatabaseAdapter adapter) {
        return new Database(adapter);
    }

    private Database(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void connect() {
        adapter.connect();
    }

    public void disconnect() {
        adapter.disconnect();
    }

    public Table createTable(String name, Column... columns) {
        return adapter.createTable(new Table(name, adapter, columns));
    }

    public void deleteTable(Table table) {
        adapter.deleteTable(table);
    }

    public Table getTable(String tableName) {
        return adapter.getTable(tableName);
    }

    public TableEntry[] getEntries(Table table) {
        return adapter.getEntries(table);
    }
}
