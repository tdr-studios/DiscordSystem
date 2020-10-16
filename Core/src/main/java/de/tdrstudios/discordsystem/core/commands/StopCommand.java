package de.tdrstudios.discordsystem.core.commands;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.Command;
import de.tdrstudios.discordsystem.api.commands.CommandSender;
import de.tdrstudios.discordsystem.api.commands.CreateCommand;
import de.tdrstudios.discordsystem.api.commands.ExecutorType;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateCommand(name = "stop", executorType = ExecutorType.CONSOLE)
public class StopCommand extends Command {

    @Override
    public void onRegister() {
        addAlias("exit");
        addAlias("shutdown");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Discord.shutdown();
    }
}
