package de.dseelp.database.api.column;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class Column {
    private String name;
    private boolean primary;
    private Class<?> clazz;

    public Column(String name, boolean primary, Class<?> clazz) {
        this.name = name;
        this.primary = primary;
        this.clazz = clazz;
    }

    public Column() {
    }

    public String getName() {
        return this.name;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
