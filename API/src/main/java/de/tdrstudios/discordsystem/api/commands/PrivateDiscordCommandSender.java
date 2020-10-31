package de.tdrstudios.discordsystem.api.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class PrivateDiscordCommandSender implements CommandSender{
    public PrivateMessageReceivedEvent getEvent() {
        return event;
    }

    private final PrivateMessageReceivedEvent event;

    public PrivateDiscordCommandSender(PrivateMessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return event.getAuthor().getName();
    }

    @Override
    public void sendMessage(String message) {
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public RestAction<Message> sendMessage(MessageEmbed embed) {
        return event.getChannel().sendMessage(embed);
    }

    @Override
    public RestAction<Message> sendMessage(EmbedBuilder embedBuilder) {
        return event.getChannel().sendMessage(embedBuilder.build());
    }

    @Override
    public RestAction<Message> sendMessage(Message message) {
        return event.getChannel().sendMessage(message);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return permission.check(this);
    }

    public User getAuthor() {
        return event.getAuthor();
    }

    public PrivateChannel getChannel() {
        return event.getChannel();
    }
}
