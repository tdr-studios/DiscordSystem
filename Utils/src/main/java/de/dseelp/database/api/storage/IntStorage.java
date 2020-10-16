package de.dseelp.database.api.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
@NoArgsConstructor
public class IntStorage implements StorageObject {
    private int i;
    @Override
    public Object get() {
        return i;
    }

    @Override
    public void set(Object s) {
        this.i = (int) s;
    }

    @Override
    public void load(String var1) {
        this.i = Integer.parseInt(var1);
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof IntStorage) {
            return get().equals(storageObject.get());
        }
        return false;
    }

    @Override
    public String getAsString() {
        return i+"";
    }
}
