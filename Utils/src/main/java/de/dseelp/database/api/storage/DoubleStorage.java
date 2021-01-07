package de.dseelp.database.api.storage;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class DoubleStorage implements StorageObject {
    private double d;

    public DoubleStorage(double d) {
        this.d = d;
    }

    public DoubleStorage() {
    }

    @Override
    public Object get() {
        return d;
    }

    @Override
    public void set(Object s) {
        this.d = (double) s;
    }

    @Override
    public void load(String var1) {
        this.d = Double.parseDouble(var1);
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof DoubleStorage) {
            return get().equals(storageObject.get());
        }
        return false;
    }

    @Override
    public String getAsString() {
        return d+"";
    }
}
