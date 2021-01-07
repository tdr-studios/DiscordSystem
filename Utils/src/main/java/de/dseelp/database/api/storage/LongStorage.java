package de.dseelp.database.api.storage;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class LongStorage implements StorageObject {
    private long l;

    public LongStorage(long l) {
        this.l = l;
    }

    public LongStorage() {
    }

    @Override
    public Object get() {
        return l;
    }

    @Override
    public void set(Object s) {
        this.l = (long) s;
    }

    @Override
    public void load(String var1) {
        this.l = Long.parseLong(var1);
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof LongStorage) {
            return l == ((LongStorage) storageObject).l;
        }
        return false;
    }

    @Override
    public String getAsString() {
        return l+"";
    }
}
