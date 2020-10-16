package de.tdrstudios.discordsystem.core.listeners;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.*;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.HandleEvents;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Arrays;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@HandleEvents
public class CommandListener {
    @EventHandler
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        CommandService service = Discord.getInstance(CommandService.class);
        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            message = message.replaceFirst("!", "");
            if (message.equals("")) return;
            String[] splitted = message.split(" ");
            CommandSender sender = new GuildDiscordCommandSender(event);
            if (splitted.length == 0) {
                service.executeCommand(sender, new String[0], message);
            }else {
                service.executeCommand(sender, Arrays.copyOfRange(splitted, 1, splitted.length), splitted[0]);
            }
        }
    }

    @EventHandler
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        CommandService service = Discord.getInstance(CommandService.class);
        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            message = message.replaceFirst("!", "");
            if (message.equals("")) return;
            String[] splitted = message.split(" ");
            CommandSender sender = new PrivateDiscordCommandSender(event);
            if (splitted.length == 0) {
                service.executeCommand(sender, new String[0], message);
            }else {
                service.executeCommand(sender, Arrays.copyOfRange(splitted, 1, splitted.length), splitted[0]);
            }
        }
    }
}
