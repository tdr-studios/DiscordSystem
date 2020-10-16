package de.dseelp.database.api.column;

import de.dseelp.database.api.storage.StorageObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ColumnEntry {
    private Column column;
    private StorageObject value;
}
