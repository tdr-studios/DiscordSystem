package de.tdrstudios.discordsystem.core.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.tdrstudios.discordsystem.api.DataService;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.commands.CommandService;
import de.tdrstudios.discordsystem.api.DiscordService;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.sound.SoundService;
import de.tdrstudios.discordsystem.console.Console;
import de.tdrstudios.discordsystem.console.LogSystem;
import de.tdrstudios.discordsystem.core.api.implementation.*;
import de.tdrstudios.discordsystem.core.api.implementation.sound.CoreSoundService;
import de.tdrstudios.discordsystem.core.system.CoreSystem;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class CoreBootstrap {
    @Getter
    private static Injector injector;
    public static void main(String[] args) {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
                bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(1));
                bind(ClassLoader.class).toInstance(CoreBootstrap.class.getClassLoader());
                bind(Console.class).to(CoreConsole.class);
                bind(CommandService.class).to(CoreCommandService.class);
                bind(DiscordService.class).to(CoreDiscordService.class);
                bind(Discord.DiscordImpl.class).to(CoreDiscordImpl.class);
                bind(ModuleService.class).to(CoreModuleService.class);
                bind(DataService.class).to(CoreDataService.class);
                bind(EventService.class).to(CoreEventService.class);
                bind(SoundService.class).to(CoreSoundService.class);
                bind(ModuleDownloadService.class).to(CoreModuleDownloadService.class);
            }
        });
        Console console = injector.getInstance(Console.class);
        injector.injectMembers(console);
        LogSystem logSystem = new LogSystem(console.getLogger());
        System.setOut(logSystem.getOut());
        System.setErr(logSystem.getError());
        Discord.DiscordImpl discord = injector.getInstance(Discord.DiscordImpl.class);
        System.out.println("Starting DiscordSystem v"+discord.getVersion()+" \""+discord.getVersionNickname()+"\"");
        injector.getInstance(CoreSystem.class).initializeSystem();
    }
}
