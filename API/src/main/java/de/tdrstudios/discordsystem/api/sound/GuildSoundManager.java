package de.tdrstudios.discordsystem.api.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public interface GuildSoundManager {
    void connect(VoiceChannel channel);
    void disconnect(Guild guild);
    AudioPlayer getAudioPlayer();
    net.dv8tion.jda.api.audio.AudioSendHandler getSendHandler();
}
