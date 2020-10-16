package de.tdrstudios.discordsystem.example;

import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.modules.CreateModule;
import de.tdrstudios.discordsystem.api.modules.Module;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateModule(name = "ExampleModule", version = "0.1-ALPA", authors = "DSeeLPYT")
public class ExampleModule extends Module {
    @Override
    public void onEnable() {
        System.out.println("ExampleModule enabled");
        final String packageString = getClass().getPackage().getName();
        Discord.scanServices(packageString);
        Discord.scanCommands(packageString);
        Discord.scanEventHandler(packageString);
    }

    @Override
    public void onDisable() {
        System.out.println("ExampleModule disabled");
    }
}
