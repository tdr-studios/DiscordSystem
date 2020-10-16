package de.tdrstudios.discordsystem.api.commands;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;

/**
 * CommandService is responsible for execute and process commands.
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateService
public interface CommandService extends Service {
    /**
     *
     * @param sender The Console/User which executed the command
     * @param args
     * @param commandName To find the command
     */
    void executeCommand(CommandSender sender, String[] args, String commandName);
    void executeCommand(CommandSender sender, String[] args, Command command);
    Command getCommand(String name);
    boolean canExecute(CommandSender sender, ExecutorType type);

    /**
     * This method scans the specified package for commands.
     * @see Discord#scanCommands(String)
     * @param packageString The package which will be scanned
     */
    void scanCommands(String... packageString);

    /**
     *
     * @param command
     * @param annotation
     * @see Discord#scanCommands(String)
     */
    void registerCommand(Command command, CreateCommand annotation);
}
