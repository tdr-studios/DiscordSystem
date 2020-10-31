package de.tdrstudios.discordsystem.api.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.tdrstudios.discordsystem.api.services.Service;
import net.dv8tion.jda.api.entities.Guild;

public interface SoundService extends Service {
    GuildSoundManager getGuildSoundManager(Guild guild);
    AudioPlayerManager getAudioPlayerManager();
}
