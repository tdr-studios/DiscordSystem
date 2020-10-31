package de.tdrstudios.discordsystem.core.commands;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.*;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@CreateCommand(name = "help", executorType = ExecutorType.ALL)
public class HelpCommand extends Command {

    @Override
    public void execute(CommandSender sender, String[] args) {
        CommandService commandService = Discord.getInstance(CommandService.class);
        Collection<Command> commands = commandService.getCommands();
        StringBuilder builder = new StringBuilder();
        for (Command command : commands) {
            if (commandService.canExecute(sender, command.getExecutorType())) {
                List<String> strings = new ArrayList<>();
                strings.addAll(Arrays.asList(command.getAliases()));
                strings.add(command.getName());
                builder.append(Arrays.toString(strings.toArray(new String[strings.size()]))+" - "+command.getDescription()+System.lineSeparator());
            }
        }
        if (sender instanceof GuildDiscordCommandSender || sender instanceof PrivateDiscordCommandSender) {
            sender.sendMessage(new EmbedBuilder().setTitle("Help").setDescription(builder.toString()).setColor(Color.CYAN)).queue();
        }else {
            builder.insert(0, "Help:"+System.lineSeparator());
            sender.sendMessage(builder.toString());
        }
    }
}
