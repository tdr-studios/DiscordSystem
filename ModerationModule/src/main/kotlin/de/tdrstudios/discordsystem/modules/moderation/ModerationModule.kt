package de.tdrstudios.discordsystem.modules.moderation

import de.tdrstudios.discordsystem.api.Discord
import de.tdrstudios.discordsystem.api.modules.*

@CreateModule(name = "Moderation", version = "1.0", authors = ["DSeeLP"], description = "This Module provides some Moderation features")
class ModerationModule() : Module() {
    override fun onEnable() {
        println("ModerationModule enabled")
    }

    @Execute(action = ModuleAction.COMMAND_READY)
    fun onCommandsReady() {
        Discord.scanCommands(ModerationModule::class.java.`package`.name)
    }

    override fun onDisable() {
        println("ModerationModule disabled")
    }
}