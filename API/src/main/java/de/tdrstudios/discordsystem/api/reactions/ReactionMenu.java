package de.tdrstudios.discordsystem.api.reactions;

import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.modules.Module;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 * @deprecated As of alpha 0.1, replaced by {@link de.tdrstudios.discordsystem.api.setup.Setup}
 */
@Deprecated
public class ReactionMenu {

    private List<ReactionAction> reactionActions = new ArrayList<>();

    public ReactionAction[] getReactionActions() {
        return reactionActions.toArray(new ReactionAction[reactionActions.size()]);
    }

    private ReactionMenu(Module module) {

    }

    public static ReactionMenu create(Module module) {
        return new ReactionMenu(module);
    }

    @Deprecated
    public void addReactionAction(ReactionAction action) {
        reactionActions.add(action);
    }

    public MessageEmbed getCancelledMessage() {
        return cancelledMessage;
    }

    public void setCancelledMessage(MessageEmbed cancelledMessage) {
        this.cancelledMessage = cancelledMessage;
    }

    private MessageEmbed cancelledMessage = new EmbedBuilder().setColor(Color.RED).setDescription("Cancelled").build();

    public Message getMessage() {
        return message;
    }

    private Message message;

    public void sendQueue(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.ORANGE);
        for (ReactionAction action : reactionActions) {
            builder.addField(action.getName(), action.getDescription() +" - "+action.getReaction().getAsUnicode(), false);
        }
        channel.sendMessage(builder.build()).queue(msg -> {
            message = msg;
            for (ReactionAction action : reactionActions) {
                msg.addReaction(action.getReaction().getAsUnicode()).queue();
            }
        });
    }

    public void cancel() {
        editMessage(cancelledMessage, true);
    }

    public void editMessage(MessageEmbed embed) {
        editMessage(embed, false);
    }

    public void editMessage(MessageEmbed embed, boolean clearReactions) {
        message.editMessage(embed).queue();
        if (clearReactions) clearReactions();
    }

    public void clearReactions() {
        message.clearReactions().queue();
    }

    public void restrictAccess(User user) {
        restrictedLong = user.getIdLong();
        restricted = true;
    }

    public void unrestrictAccess() {
        restricted = false;
    }

    private long restrictedLong;

    private boolean restricted;

    @EventHandler
    public void onReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        if (restricted) {
            if (restrictedLong != event.getUser().getIdLong()) return;
        }
        MessageReaction reaction = event.getReaction();
        MessageReaction.ReactionEmote reactionEmote = reaction.getReactionEmote();
        if (reactionEmote.isEmote()) return;
        if (reaction.getMessageIdLong() == message.getIdLong()) {
            reaction.removeReaction(event.getUser()).queue();
            for (ReactionAction action : reactionActions) {
                if (action.getReaction().getAsUnicode().equals(reactionEmote.getEmoji())) {
                    clearReactions();
                    action.getSubmitAction().accept(this);
                }
            }
        }
    }
}
