package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.*;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.event.events.internal.CommandRegisterEvent;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.utils.ClassCriteria;
import de.tdrstudios.discordsystem.utils.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService
public class CoreCommandService implements CommandService {
    private BiMap<Command, CreateCommand> commands = HashBiMap.create();
    @Inject
    private ExecutorService executorService;
    @Override
    public void executeCommand(CommandSender sender, String[] args, String commandName) {
        Command command = getCommand(commandName);
        if (command == null) return;
        executeCommand(sender, args, command);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args, Command command) {
        if (!commands.containsKey(command)) return;
        ExecutorType executorType = commands.get(command).executorType();
        boolean canExecute = canExecute(sender, executorType);
        if (canExecute) executorService.execute(() -> command.execute(sender, args));
    }

    @Override
    public Command getCommand(String name) {
        name = name.toLowerCase();
        for (Map.Entry<Command, CreateCommand> entry : commands.entrySet()) {
            if (entry.getValue().name().toLowerCase().equals(name)) return entry.getKey();
            for (String alias : entry.getKey().getAliases()) {
                if (alias.toLowerCase().equals(name)) return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean canExecute(CommandSender sender, ExecutorType type) {
        switch (type) {
            case ALL: return true;
            case GUILD: return sender instanceof GuildDiscordCommandSender;
            case PRIVATE: return sender instanceof PrivateDiscordCommandSender;
            case CONSOLE: return sender instanceof ConsoleCommandSender;
            case GUILD_AND_CONSOLE: return sender instanceof GuildDiscordCommandSender || sender instanceof ConsoleCommandSender;
            case PRIVATE_AND_CONSOLE: return sender instanceof PrivateDiscordCommandSender || sender instanceof ConsoleCommandSender;
            case GUILD_AND_PRIVATE: return sender instanceof GuildDiscordCommandSender || sender instanceof PrivateDiscordCommandSender;
            default: return false;
        }
    }

    @Override
    public void scanCommands(String... packageString) {
        System.out.println("Scanning "+ Arrays.toString(packageString) +" for Commands...");
        EventService eventService = Discord.getInstance(EventService.class);
        int count = 0;
        for (Class<?> clazz : ReflectionUtils.filter(packageString, ClassCriteria.annotatedWith(CreateCommand.class), ClassCriteria.subclassOf(Command.class))) {
            Command command = (Command) Discord.getInstance(clazz);
            CreateCommand annotation = clazz.getAnnotation(CreateCommand.class);
            command.setName(annotation.name());
            command.setExecutorType(annotation.executorType());
            command.onRegister();
            command.setName(annotation.name());
            command.setExecutorType(annotation.executorType());
            registerCommand(command, annotation);
            eventService.scanForEvents(clazz);
            count++;
        }
        System.out.println("Found "+count+" Commands in "+ Arrays.toString(packageString) +"!");
    }

    @Override
    public void registerCommand(Command command, CreateCommand annotation) {
        final String name = annotation.name().toLowerCase();
        for (Map.Entry<Command, CreateCommand> entry : commands.entrySet()) {
            if (entry.getValue().name().toLowerCase().equals(name)) return;
            for (String alias : entry.getKey().getAliases()) {
                alias = alias.toLowerCase();
                if (alias.equals(name)) return;
                for (String commandAlias : command.getAliases()) {
                    if (commandAlias.equals(alias)) return;
                }
            }
        }
        commands.put(command, annotation);
        Discord.getInstance(ExecutorService.class).execute(()->Discord.getInstance(EventService.class).callEvent(new CommandRegisterEvent(command, annotation)));
    }

    @Override
    public Collection<Command> getCommands() {
        return commands.keySet();
    }

    @Override
    public void initialize() throws Exception {
        Discord.getInstance(ModuleService.class).callAction(ModuleAction.COMMAND_READY);
    }
}
