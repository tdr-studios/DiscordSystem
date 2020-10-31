package de.tdrstudios.discordsystem.modules.moderation.commands

import de.tdrstudios.discordsystem.api.DataService
import de.tdrstudios.discordsystem.api.Discord
import de.tdrstudios.discordsystem.api.commands.*
import de.tdrstudios.discordsystem.utils.JsonDocument
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color
import java.time.Instant
import java.util.concurrent.TimeUnit

@CreateCommand(name = "suggestion", executorType = ExecutorType.GUILD)
class SuggestionCommand : Command() {
    override fun onRegister() {
        addAlias("vorschlag")
    }

    override fun execute(commandSender: CommandSender?, args: Array<out String>?) {
        val sender: GuildDiscordCommandSender = commandSender as GuildDiscordCommandSender
        sender.event.message.delete().queue()
        val guildId = sender.guild.idLong
        if (args!!.size > 1) {
            var guildData:JsonDocument = Discord.getInstance(DataService::class.java).getGuildData(commandSender.guild.idLong)
            if (guildData.has("moderation.suggestions.channel")) {
                var long: Long = guildData.getLong("moderation.suggestions.channel")
                var textChannel: TextChannel? = sender.guild.getTextChannelById(long)
                if (textChannel != null) {
                    var builder: EmbedBuilder = EmbedBuilder()
                    builder.setColor(Color.ORANGE)
                    builder.setDescription("Suggestion by "+ sender.author.asMention);
                    builder.addField("Area:", args[0], false)
                    builder.setTimestamp(Instant.now())
                    val sBuilder: StringBuilder = StringBuilder()
                    val copiedArgs = args.copyOfRange(1, args.size)
                    for (i in copiedArgs.indices) {
                        var s = copiedArgs[i]
                        sBuilder.append(s)
                        if (!(i == 0 && copiedArgs.size == 1))
                            if (copiedArgs.size-1 != i) sBuilder.append(" ")

                    }
                    builder.addField("Your Idea:", sBuilder.toString(), false)
                    builder.setThumbnail(sender.author.avatarUrl)
                    textChannel.sendMessage(builder.build()).queue()
                }else sender.sendMessage(EmbedBuilder().setColor(Color.RED).setDescription("The suggestionChannel is not set! Please use "+ Discord.getPrefix(guildId) +"moderationsetup to set it")).queue()
            }else {
                sender.sendMessage(EmbedBuilder().setColor(Color.RED).setDescription("The suggestionChannel is not set! Please use "+ Discord.getPrefix(guildId) +"moderationsetup to set it")).queue()
            }
        }else{
            sender.sendMessage(EmbedBuilder().setDescription("Use "+Discord.getPrefix(guildId))).queue { t -> t.delete().queueAfter(5, TimeUnit.SECONDS) }
        }
    }
}