package de.dseelp.database.api.storage;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface StorageObject {
    Object get();
    void set(Object var1);
    void load(String var1);
    boolean compare(StorageObject storageObject);
    String getAsString();
}
