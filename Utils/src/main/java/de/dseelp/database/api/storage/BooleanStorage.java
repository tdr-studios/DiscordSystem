package de.dseelp.database.api.storage;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class BooleanStorage implements StorageObject {
    private boolean b;

    public BooleanStorage(boolean b) {
        this.b = b;
    }

    public BooleanStorage() {
    }

    @Override
    public Object get() {
        return b;
    }

    @Override
    public void set(Object s) {
        this.b = (boolean) s;
    }

    @Override
    public void load(String var1) {
        this.b = Boolean.parseBoolean(var1);
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof BooleanStorage) {
            return get().equals(storageObject.get());
        }
        return false;
    }

    @Override
    public String getAsString() {
        return b+"";
    }
}
