package de.tdrstudios.discordsystem.api.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.RestAction;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface CommandSender {
    String getName();
    void sendMessage(String message);
    RestAction<Message> sendMessage(MessageEmbed embed);
    RestAction<Message> sendMessage(EmbedBuilder embedBuilder);
    RestAction<Message> sendMessage(Message message);

    boolean hasPermission(Permission permission);
}
