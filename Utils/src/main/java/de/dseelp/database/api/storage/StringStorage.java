package de.dseelp.database.api.storage;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class StringStorage implements StorageObject {
    private String s;

    public StringStorage(String s) {
        this.s = s;
    }

    public StringStorage() {
    }

    @Override
    public Object get() {
        return s;
    }

    @Override
    public void set(Object s) {
        this.s = (String) s;
    }

    @Override
    public void load(String var1) {
        this.s = var1;
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof StringStorage) {
            return get().equals(storageObject.get());
        }
        return false;
    }

    @Override
    public String getAsString() {
        return s;
    }
}
