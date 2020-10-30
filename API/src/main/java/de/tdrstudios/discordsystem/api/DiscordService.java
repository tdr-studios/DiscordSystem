package de.tdrstudios.discordsystem.api;

import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface DiscordService extends Service {
    ShardManager getShardManager();
    void start();
    void stop();
}
