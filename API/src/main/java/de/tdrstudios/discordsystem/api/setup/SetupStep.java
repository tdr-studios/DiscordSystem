package de.tdrstudios.discordsystem.api.setup;


import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.EventService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface SetupStep {
    void execute(Setup setup, MessageChannel channel);
    void setUserId(long id);
    void cancel();
    String getIdentifier();

    static class ChannelSetupStep implements SetupStep {
        private Setup setup;
        private final String identifier;
        private Message message;
        private long userId = -1;

        public ChannelSetupStep(Message message, String identifier) {
            this.identifier = identifier;
            this.message = message;

        }

        public ChannelSetupStep(MessageEmbed embed, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(embed).setEmbed(embed).build();
        }

        public ChannelSetupStep(EmbedBuilder builder, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(builder).setEmbed(builder.build()).build();
        }

        @Override
        public void execute(Setup setup, MessageChannel channel) {
            this.setup = setup;
            Discord.getInstance(EventService.class).addListener(this);
            channel.sendMessage(message).queue(message -> this.message = message);
        }

        @Override
        public void setUserId(long id) {
            this.userId = id;
        }

        @Override
        public void cancel() {
            Discord.getInstance(EventService.class).removeListener(this);
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @EventHandler
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getJDA().getSelfUser().getIdLong() == event.getAuthor().getIdLong()) return;
            if (event.isFromType(setup.isPrivateChannel() ? ChannelType.PRIVATE : ChannelType.TEXT)) {
                MessageChannel channel = setup.isPrivateChannel() ? setup.getPrivateChannel(event.getJDA()) : setup.getTextChannel(event.getGuild());
                List<TextChannel> mentionedChannels = event.getMessage().getMentionedChannels();
                if (userId != -1 && event.getAuthor().getIdLong() != userId) {
                    if (mentionedChannels.size() > 0) {
                        channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("You are not allowed to execute this setup").build()).queue();
                    }
                    return;
                }
                if (mentionedChannels.size() == 1) {
                    Discord.getInstance(EventService.class).removeListener(this);
                    setup.completeStep(mentionedChannels.get(0).getIdLong());
                }
            }
        }
    }

    static class YesNoStep implements SetupStep {
        private Setup setup;
        private final String identifier;
        private Message message;
        private long userId = -1;

        public YesNoStep(Message message, String identifier) {
            this.identifier = identifier;
            this.message = message;

        }

        public YesNoStep(MessageEmbed embed, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(embed).setEmbed(embed).build();
        }

        public YesNoStep(EmbedBuilder builder, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(builder).setEmbed(builder.build()).build();
        }

        @Override
        public void execute(Setup setup, MessageChannel channel) {
            this.setup = setup;
            Discord.getInstance(EventService.class).addListener(this);
            channel.sendMessage(message).queue(message -> {
                this.message = message;
                message.addReaction("✅").queue();
                message.addReaction("❌").queue();
            });
        }

        @Override
        public void setUserId(long id) {
            this.userId = id;
        }

        @Override
        public void cancel() {
            Discord.getInstance(EventService.class).removeListener(this);
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @EventHandler
        public void onMessageReactionAddEvent(MessageReactionAddEvent event) {
            if (event.getJDA().getSelfUser().getIdLong() == event.getUser().getIdLong()) return;
            System.out.println("reaction add");
            if (event.isFromType(setup.isPrivateChannel() ? ChannelType.PRIVATE : ChannelType.TEXT)) {
                MessageChannel channel = setup.isPrivateChannel() ? setup.getPrivateChannel(event.getJDA()) : setup.getTextChannel(event.getGuild());
                if (event.getUser() == null) return;
                if (userId != -1 && event.getUser().getIdLong() != userId) {
                    channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("You are not allowed to execute this setup").build()).queue();
                    return;
                }
                MessageReaction reaction = event.getReaction();
                if (message.getIdLong() == reaction.getMessageIdLong()) {
                    channel.retrieveMessageById(message.getIdLong()).queue(message -> {
                        reaction.removeReaction(event.getUser()).queue();
                        if (!reaction.getReactionEmote().isEmoji()) return;
                        if (reaction.getReactionEmote().getEmoji().equals("✅")) {
                            System.out.println("OK");
                            Discord.getInstance(EventService.class).removeListener(this);
                            setup.completeStep(true);
                        }else if (reaction.getReactionEmote().getEmoji().equals("❌")) {
                            System.out.println("Nope");
                            Discord.getInstance(EventService.class).removeListener(this);
                            setup.completeStep(false);
                        }
                    });
                }
            }
        }
    }
}
