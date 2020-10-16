package de.dseelp.database.api.table;

import de.dseelp.database.api.column.ColumnEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableEntry {
    private List<ColumnEntry> entries;

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
}
