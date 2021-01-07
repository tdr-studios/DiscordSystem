package de.tdrstudios.discordsystem.api.setup;


import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.EventService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

        public void completeAction(Consumer<StepAction<MessageChannel>> action) {
            completeAction = action;
        }

        private Consumer<StepAction<MessageChannel>> completeAction = new Consumer<StepAction<MessageChannel>>() {
            @Override
            public void accept(StepAction<MessageChannel> action) {
                action.getSetup().completeStep(action.getValue().getIdLong());
            }
        };

        public void cancelAction(Consumer<StepAction<Void>> action) {
            cancelAction = action;
        }

        private Consumer<StepAction<Void>> cancelAction = new Consumer<StepAction<Void>>() {
            @Override
            public void accept(StepAction<Void> voidStepAction) {
                setup.cancel();
            }
        };

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
                    final MessageChannel ch = mentionedChannels.get(0);
                    completeAction.accept(new StepAction<MessageChannel>() {
                        @Override
                        public MessageChannel getValue() {
                            return ch;
                        }

                        @Override
                        public Setup getSetup() {
                            return setup;
                        }

                        @Override
                        public SetupStep getStep() {
                            return ChannelSetupStep.this;
                        }
                    });
                }else if (event.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
                    cancelAction.accept(new StepAction<Void>() {
                        @Override
                        public Void getValue() {
                            return null;
                        }

                        @Override
                        public Setup getSetup() {
                            return setup;
                        }

                        @Override
                        public SetupStep getStep() {
                            return ChannelSetupStep.this;
                        }
                    });
                }
            }
        }
    }

    static class EmojiStep implements SetupStep {
        private Setup setup;
        private final String identifier;
        private Message message;
        private long userId = -1;

        public EmojiStep(Message message, String identifier) {
            this.identifier = identifier;
            this.message = message;

        }

        public EmojiStep(MessageEmbed embed, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(embed).setEmbed(embed).build();
        }

        public EmojiStep(EmbedBuilder builder, String identifier) {
            this.identifier = identifier;
            this.message = new MessageBuilder(builder).setEmbed(builder.build()).build();
        }

        public EmojiStep(Message message) {
            this.identifier = RandomStringUtils.randomAlphanumeric(16);
            this.message = message;

        }

        public EmojiStep(MessageEmbed embed) {
            this.identifier = RandomStringUtils.randomAlphanumeric(16);
            this.message = new MessageBuilder(embed).setEmbed(embed).build();
        }

        public EmojiStep(EmbedBuilder builder) {
            this.identifier = RandomStringUtils.randomAlphanumeric(16);
            this.message = new MessageBuilder(builder).setEmbed(builder.build()).build();
        }

        private List<EmojiAction> reactions = new ArrayList<>();

        public EmojiStep reaction(String emoji, Consumer<StepAction<String>> consumer) {
            reactions.add(new EmojiAction(emoji, consumer));
            return this;
        }

        private class EmojiAction {
            public EmojiAction(String emoji, Consumer<StepAction<String>> executor) {
                this.emoji = emoji;
                this.executor = executor;
            }

            public String getEmoji() {
                return emoji;
            }

            public Consumer<StepAction<String>> getExecutor() {
                return executor;
            }

            private String emoji;
            private Consumer<StepAction<String>> executor;
        }

        @Override
        public void execute(Setup setup, MessageChannel channel) {
            this.setup = setup;
            Discord.getInstance(EventService.class).addListener(this);
            channel.sendMessage(message).queue(message -> {
                this.message = message;
                reactions.forEach((a) -> message.addReaction(a.getEmoji()).queue());
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
            if (event.getUser() == null) return;
            if (event.getJDA().getSelfUser().getIdLong() == event.getUser().getIdLong()) return;
            if (event.isFromType(setup.isPrivateChannel() ? ChannelType.PRIVATE : ChannelType.TEXT)) {
                MessageChannel channel = setup.isPrivateChannel() ? setup.getPrivateChannel(event.getJDA()) : setup.getTextChannel(event.getGuild());
                MessageReaction reaction = event.getReaction();
                if (message.getIdLong() == reaction.getMessageIdLong()) {
                    if (reaction.getChannel() instanceof TextChannel) reaction.removeReaction(event.getUser()).queue();
                    if (userId != -1 && event.getUser().getIdLong() != userId) {
                        channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("You are not allowed to execute this setup "+event.getUser().getAsMention()).build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }
                    channel.retrieveMessageById(message.getIdLong()).queue(message -> {
                        if (!reaction.getReactionEmote().isEmoji()) return;
                        String emoji = reaction.getReactionEmote().getEmoji();
                        for (EmojiAction action : reactions) {
                            if (action.getEmoji().equals(emoji)) {
                                Discord.getInstance(EventService.class).removeListener(this);
                                action.getExecutor().accept(new StepAction<String>() {
                                    @Override
                                    public String getValue() {
                                        return emoji;
                                    }

                                    @Override
                                    public Setup getSetup() {
                                        return setup;
                                    }

                                    @Override
                                    public SetupStep getStep() {
                                        return EmojiStep.this;
                                    }
                                });
                                return;
                            }
                        }
                    });
                }
            }
        }
    }
}
