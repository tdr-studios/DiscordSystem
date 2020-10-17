package de.tdrstudios.discordsystem.api.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.RestAction;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ConsoleCommandSender implements CommandSender {
    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public RestAction<Message> sendMessage(MessageEmbed embed) {
        throw new UnsupportedOperationException("This method is only supported in Discord commandsenders");
    }

    @Override
    public RestAction<Message> sendMessage(EmbedBuilder embedBuilder) {
        throw new UnsupportedOperationException("This method is only supported in Discord commandsenders");
    }

    @Override
    public RestAction<Message> sendMessage(Message message) {
        throw new UnsupportedOperationException("This method is only supported in Discord commandsenders");
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return true;
    }
}
