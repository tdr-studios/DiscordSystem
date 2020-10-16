package de.tdrstudios.discordsystem.api.reactions;

import com.vdurmont.emoji.Emoji;
import net.dv8tion.jda.api.entities.Emote;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface Reaction {
    String getAsUnicode();
    Emote getAsEmote();
    Emoji getAsEmoji();
}
