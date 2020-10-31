package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.DiscordService;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.console.Console;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService(Integer.MAX_VALUE)
public class CoreDiscordImpl implements Discord.DiscordImpl {
    @Override
    public void shutdown() {
        Discord.getInstance(ModuleService.class).disableAll();
        Discord.getInstance(DiscordService.class).stop();
        Console console = Discord.getInstance(Console.class);
        console.stopReading();
        console.stopLoop();
        System.exit(0);
    }

    @Override
    public String getVersion() {
        return "0.2-ALPHA";
    }

    @Override
    public String getVersionNickname() {
        return "Grapefruit";
    }

    @Override
    public void initialize() throws Exception {

    }
}
