package de.dseelp.database.api.column;

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
public class Column {
    private String name;
    private boolean primary;
    private Class<?> clazz;
}
