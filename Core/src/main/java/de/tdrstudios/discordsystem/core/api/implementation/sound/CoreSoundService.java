package de.tdrstudios.discordsystem.core.api.implementation.sound;

import com.google.inject.Singleton;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.sound.GuildSoundManager;
import de.tdrstudios.discordsystem.api.sound.SoundService;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

@Singleton
@CreateService(0)
public class CoreSoundService implements SoundService {
    private AudioPlayerManager playerManager;
    private Map<Long, GuildSoundManager> musicManagers;
    @Override
    public GuildSoundManager getGuildSoundManager(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildSoundManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new CoreGuildSoundManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public AudioPlayerManager getAudioPlayerManager() {
        return playerManager;
    }

    @Override
    public void initialize() throws Exception {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        musicManagers = new HashMap<>();
    }
}
