package de.tdrstudios.discordsystem.core.system;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.event.events.console.ConsoleMessageReadEvent;
import de.tdrstudios.discordsystem.api.DiscordService;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.console.Console;
import de.tdrstudios.discordsystem.console.LogSystem;

import javax.annotation.PostConstruct;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
public class CoreSystem {

    private final Injector injector;

    @Inject
    public CoreSystem(Injector injector) {
        this.injector = injector;
        injector.injectMembers(this);
    }

    @PostConstruct
    public void initializeSystem() {
        Discord.setInjector(injector);
        Console console = Discord.getInstance(Console.class);
        console.startLoop();
        console.startReading();
        final EventService eventRepository = Discord.getInstance(EventService.class);
        console.setMessageConsumer(message -> eventRepository.callEvent(new ConsoleMessageReadEvent(message)));
        //((CoreModuleManager) Discord.getInstance(ModuleManager.class)).setInjector(injector);
        final String scanPackage = "de.tdrstudios.discordsystem";
        Discord.scanServices(scanPackage);
        Discord.scanEventHandler(scanPackage);
        Discord.scanCommands(scanPackage);
        DiscordService discordService = Discord.getInstance(DiscordService.class);
        discordService.start();
        Discord.getInstance(ModuleService.class).startModules();
    }
}
