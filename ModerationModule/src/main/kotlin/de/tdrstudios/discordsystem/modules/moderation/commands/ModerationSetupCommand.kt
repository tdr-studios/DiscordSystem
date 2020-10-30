package de.tdrstudios.discordsystem.modules.moderation.commands

import de.tdrstudios.discordsystem.api.DataService
import de.tdrstudios.discordsystem.api.Discord
import de.tdrstudios.discordsystem.api.commands.*
import de.tdrstudios.discordsystem.api.setup.Setup
import de.tdrstudios.discordsystem.api.setup.SetupStep
import de.tdrstudios.discordsystem.utils.JsonDocument
import net.dv8tion.jda.api.EmbedBuilder

@CreateCommand(name = "moderationsetup", executorType = ExecutorType.GUILD)
class ModerationSetupCommand : Command() {
    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        val sender: GuildDiscordCommandSender = commandSender as GuildDiscordCommandSender
        if (!sender.hasPermission(Permission.guildPermission(net.dv8tion.jda.api.Permission.MANAGE_CHANNEL))) return
        ModerationSetup(sender.guild.idLong).start(sender.channel)
    }

    class ModerationSetup(guildId: Long) : Setup() {
        val guildId: Long = guildId;

        init {
            //addStep(SetupStep.YesNoStep(EmbedBuilder().setTitle("Suggestions").setDescription("Do you want Suggestions?"), "suggestions.enabled"))
            //addStep(SetupStep.YesNoStep(EmbedBuilder().setTitle("BugReports").setDescription("Do you want Suggestions?"), "suggestions.enabled"))
            addStep(SetupStep.ChannelSetupStep(EmbedBuilder().setTitle("Suggestions").setDescription("Please tag the channel you want suggestions to send in"), "suggestions.channel"))
            addStep(SetupStep.ChannelSetupStep(EmbedBuilder().setTitle("Bugreports").setDescription("Please tag the channel you want bugreports to send in"), "bugreports.channel"))
        }

        override fun onComplete(document: JsonDocument) {
            val dataService: DataService = Discord.getInstance(DataService::class.java)
            val guildData: JsonDocument = dataService.getGuildData(guildId)
            if (true || document.getBoolean("suggestions.enabled")) {
                guildData.add("moderation.suggestions.channel", document.getLong("suggestions.channel"))
                guildData.add("moderation.suggestions.enabled", true)
            }else guildData.add("moderation.suggestions.enabled", false)
            if (true || document.getBoolean("bugreports.enabled")) {
                guildData.add("moderation.bugreports.channel", document.getLong("bugreports.channel"))
                guildData.add("moderation.bugreports.enabled", true)
            }else guildData.add("moderation.bugreports.enabled", false)
            dataService.saveGuildData(guildId, guildData)
        }

        override fun onCancel() {

        }
    }
}