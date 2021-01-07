package de.tdrstudios.discordsystem.api.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class GuildDiscordCommandSender implements MessageCommandSender<TextChannel> {
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    private final GuildMessageReceivedEvent event;

    public GuildDiscordCommandSender(GuildMessageReceivedEvent event) {
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

    public TextChannel getChannel() {
        return event.getChannel();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public Member getMember() {
        return event.getMember();
    }
}
