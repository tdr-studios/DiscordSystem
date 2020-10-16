package de.tdrstudios.discordsystem.example;

import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.HandleEvents;
import de.tdrstudios.discordsystem.api.event.events.console.ConsoleMessageReadEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

@HandleEvents
public class TestEventHandler {
    @EventHandler
    public void onGuildMessage(GuildMessageReactionAddEvent event) {
        System.out.println("Message sent with Id: "+event.getMessageId());
    }

    @EventHandler
    public void onConsoleMessage(ConsoleMessageReadEvent event) {
        System.out.println("Console Read: "+event.getMessage());
    }
}
