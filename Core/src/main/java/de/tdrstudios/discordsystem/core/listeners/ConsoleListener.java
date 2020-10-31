package de.tdrstudios.discordsystem.core.listeners;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.CommandSender;
import de.tdrstudios.discordsystem.api.commands.CommandService;
import de.tdrstudios.discordsystem.api.commands.ConsoleCommandSender;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.HandleEvents;
import de.tdrstudios.discordsystem.api.event.events.console.ConsoleMessageReadEvent;

import java.util.Arrays;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@HandleEvents
public class ConsoleListener {
    @EventHandler
    public void onConsoleRead(ConsoleMessageReadEvent event) {
        CommandService service = Discord.getInstance(CommandService.class);
        final String message = event.getMessage();
        if (message.equals("")) return;
        String[] splitted = message.split(" ");
        CommandSender sender = new ConsoleCommandSender();
        if (splitted.length == 0) {
            service.executeCommand(sender, new String[0], message);
        }else {
            service.executeCommand(sender, Arrays.copyOfRange(splitted, 1, splitted.length), splitted[0]);
        }
    }
}
