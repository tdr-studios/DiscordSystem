package de.tdrstudios.discordsystem.api.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface MessageCommandSender<T extends MessageChannel> extends CommandSender {
    User getAuthor();
    T getChannel();
}
