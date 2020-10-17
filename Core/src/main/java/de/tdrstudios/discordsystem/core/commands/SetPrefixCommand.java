package de.tdrstudios.discordsystem.core.commands;

import de.tdrstudios.discordsystem.api.DataService;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.*;
import de.tdrstudios.discordsystem.utils.JsonDocument;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

@CreateCommand(name = "prefix", executorType = ExecutorType.GUILD)
public class SetPrefixCommand extends Command {

    @Override
    public void onRegister() {
        setDescription("This command change the prefix of the bot in your guild");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        GuildDiscordCommandSender sender = (GuildDiscordCommandSender) commandSender;
        DataService dataService = Discord.getInstance(DataService.class);
        JsonDocument guildData = dataService.getGuildData(sender.getGuild().getIdLong());
        if (args.length == 0) {
            sender.sendMessage(new EmbedBuilder().setTitle("Prefix").setDescription("The current prefix is: "+Discord.getPrefix(sender.getGuild().getIdLong())).setColor(Color.CYAN)).queue();
        }else {
            if (!commandSender.hasPermission(Permission.guildPermission(net.dv8tion.jda.api.Permission.MANAGE_SERVER))) {
                sender.sendMessage(new EmbedBuilder().setTitle("Prefix").setDescription("You have no permission to execute this command!").setColor(Color.RED)).queue();
                return;
            }
            if (args[0].length() > 10) {
                sender.sendMessage(new EmbedBuilder().setTitle("Prefix").setDescription("This prefix is too long!").setColor(Color.RED)).queue();
                return;
            }
            guildData.add("prefix", args[0].toLowerCase());
            dataService.saveGuildData(sender.getGuild().getIdLong(), guildData);
            sender.sendMessage(new EmbedBuilder().setTitle("Prefix").setDescription("You set the prefix to: "+args[0]).setColor(Color.green)).queue();
        }
    }
}
