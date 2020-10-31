package de.tdrstudios.discordsystem.core.api.implementation.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import de.tdrstudios.discordsystem.api.sound.GuildSoundManager;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CoreGuildSoundManager implements GuildSoundManager {

    private final AudioPlayer audioPlayer;

    public CoreGuildSoundManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
    }

    public void addListener(AudioEventListener listener) {
        audioPlayer.addListener(listener);
    }

    @Override
    public void connect(VoiceChannel channel) {
        channel.getGuild().getAudioManager().openAudioConnection(channel);
    }

    @Override
    public void disconnect(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
    }

    @Override
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    @Override
    public AudioSendHandler getSendHandler() {
        return new de.tdrstudios.discordsystem.core.api.implementation.sound.AudioSendHandler(audioPlayer);
    }
}
