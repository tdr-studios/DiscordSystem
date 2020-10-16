package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import de.dseelp.database.api.Database;
import de.dseelp.database.api.adapter.JsonAdapter;
import de.dseelp.database.api.column.Column;
import de.dseelp.database.api.storage.LongStorage;
import de.dseelp.database.api.storage.StringStorage;
import de.dseelp.database.api.table.Table;
import de.dseelp.database.api.table.TableEntry;
import de.tdrstudios.discordsystem.api.DataService;
import de.tdrstudios.discordsystem.utils.JsonDocument;

import java.io.File;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
public class CoreDataService implements DataService {
    private Database database;

    @Override
    public JsonDocument getGuildData(long guildId) {
        TableEntry guildData = database.getTable("guildData").getEntry(new LongStorage(guildId));
        if (guildData == null) {
            saveGuildData(guildId, new JsonDocument());
            return getGuildData(guildId);
        }
        StringStorage jsonStorage = (StringStorage) guildData.get("json").getValue();
        return new JsonDocument(JsonParser.parseString(jsonStorage.getAsString()).getAsJsonObject());
    }

    @Override
    public void saveGuildData(long guildId, JsonDocument data) {
        Table table = database.getTable("guildData");
        table.addEntry(new LongStorage(guildId), new StringStorage(data.toString()));
    }

    @Override
    public void initialize() throws Exception {
        database = Database.createDatabase(new JsonAdapter(true, new File("guildData.json")));
        database.connect();
        database.createTable("guildData", new Column("id", true, LongStorage.class), new Column("json", false, StringStorage.class));
    }
}
