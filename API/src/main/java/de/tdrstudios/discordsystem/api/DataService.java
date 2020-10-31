package de.tdrstudios.discordsystem.api;

import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;
import de.tdrstudios.discordsystem.utils.JsonDocument;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface DataService extends Service {
    JsonDocument getGuildData(long guildId);
    void saveGuildData(long guildId, JsonDocument data);
}
