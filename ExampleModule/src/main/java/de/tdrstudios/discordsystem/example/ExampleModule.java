package de.tdrstudios.discordsystem.example;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.modules.CreateModule;
import de.tdrstudios.discordsystem.api.modules.Execute;
import de.tdrstudios.discordsystem.api.modules.Module;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateModule(name = "ExampleModule", version = "0.2-ALPHA", authors = "DSeeLPYT")
public class ExampleModule extends Module {
    @Execute(action = ModuleAction.DISCORD_READY)
    public void enable() {
        System.out.println("ExampleModule enabled");
        final String packageString = getClass().getPackage().getName();
        Discord.scanServices(packageString);
        Discord.scanCommands(packageString);
        Discord.scanEventHandler(packageString);
    }

    @Execute(action = ModuleAction.DISABLE)
    public void disable() {
        System.out.println("ExampleModule disabled");
    }
}
