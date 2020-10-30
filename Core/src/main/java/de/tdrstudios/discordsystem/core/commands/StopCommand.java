package de.tdrstudios.discordsystem.core.commands;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.*;
import de.tdrstudios.discordsystem.utils.JsonConfig;

import java.util.Collection;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateCommand(name = "stop", executorType = ExecutorType.ALL)
public class StopCommand extends Command {

    @Override
    public void onRegister() {
        addAlias("exit");
        addAlias("shutdown");
        setDescription("Shutdown the command");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JsonConfig config = Discord.getConfig();
        Collection<Long> adminIds = config.getLongList("adminIds");
        boolean ok = false;
        for (long adminId : adminIds) {
            if (sender.hasPermission(Permission.userIdPermission(adminId))) ok = true;
        }
        System.gc();
        if (ok) Discord.shutdown();
    }
}
