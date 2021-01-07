package de.dseelp.database.api.column;

import de.dseelp.database.api.storage.StorageObject;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ColumnEntry {
    private Column column;
    private StorageObject value;

    public ColumnEntry(Column column, StorageObject value) {
        this.column = column;
        this.value = value;
    }

    public ColumnEntry() {
    }

    public Column getColumn() {
        return this.column;
    }

    public StorageObject getValue() {
        return this.value;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public void setValue(StorageObject value) {
        this.value = value;
    }
}
