package de.dseelp.database.api.storage;

import java.util.UUID;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class UUIDStorage implements StorageObject{

    public UUIDStorage(UUID uuid) {
        this.uuid = uuid;
    }

    public UUIDStorage() {
    }

    private UUID uuid;

    @Override
    public Object get() {
        return uuid;
    }

    @Override
    public void set(Object uuid) {
        this.uuid = (UUID) uuid;
    }

    @Override
    public void load(String s) {
        uuid = UUID.fromString(s);
    }

    @Override
    public boolean compare(StorageObject storageObject) {
        if (storageObject instanceof UUIDStorage) {
            return get().equals(storageObject.get());
        }
        return false;
    }

    @Override
    public String getAsString() {
        return uuid.toString();
    }
}
