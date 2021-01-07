package de.dseelp.database.api.table;

import de.dseelp.database.api.column.ColumnEntry;

import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class TableEntry {
    private List<ColumnEntry> entries;

    public TableEntry(List<ColumnEntry> entries) {
        this.entries = entries;
    }

    public TableEntry() {
    }

    public ColumnEntry getPrimary() {
        for (ColumnEntry entry : entries) {
            if (entry.getColumn().isPrimary()) return entry;
        }
        return null;
    }

    public ColumnEntry get(String name) {
        name = name.toLowerCase();
        for (ColumnEntry entry : entries) {
            if (entry.getColumn().getName().toLowerCase().equals(name)) return entry;
        }
        return null;
    }

    public List<ColumnEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<ColumnEntry> entries) {
        this.entries = entries;
    }
}
