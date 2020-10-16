package de.tdrstudios.discordsystem.api.reactions;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.api.entities.Emote;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class EmojiReaction implements Reaction {

    private final Emoji emoji;

    public EmojiReaction(String emoji) {
        this.emoji = EmojiManager.getByUnicode(emoji);
    }

    @Override
    public String getAsUnicode() {
        return emoji.getUnicode();
    }

    @Override
    public Emote getAsEmote() {
        return null;
    }

    @Override
    public Emoji getAsEmoji() {
        return emoji;
    }
}
