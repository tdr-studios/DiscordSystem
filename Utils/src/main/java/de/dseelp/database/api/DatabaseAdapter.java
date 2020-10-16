package de.dseelp.database.api;

import de.dseelp.database.api.column.ColumnEntry;
import de.dseelp.database.api.table.Table;
import de.dseelp.database.api.table.TableEntry;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface DatabaseAdapter {
    void connect();
    void disconnect();

    Table createTable(Table table);
    void deleteTable(Table table);
    Table getTable(String tableName);
    boolean tableExists(String table);

    void addEntry(Table table, TableEntry entry);
    void updateEntry(Table table, TableEntry entry);
    void deleteEntry(Table table, TableEntry entry);
    TableEntry getEntry(Table table, ColumnEntry key);
    TableEntry[] getEntries(Table table);
}
