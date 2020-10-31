package de.tdrstudios.discordsystem.core.system;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.event.events.console.ConsoleMessageReadEvent;
import de.tdrstudios.discordsystem.api.event.events.internal.ApplicationReadyEvent;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.console.Console;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;

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

    private long startTime;

    @PostConstruct
    public void initializeSystem() {
        startTime = System.currentTimeMillis();
        Discord.setInjector(injector);
        Console console = Discord.getInstance(Console.class);
        console.setPrompt("&c"+System.getProperty("user.name")+"&r@&7"+Discord.getVersion()+" &f=> &r");
        console.startLoop();
        console.startReading();
        Discord.getInstance(ModuleService.class).startModules();
        final EventService eventRepository = Discord.getInstance(EventService.class);
        eventRepository.scanForEvents(this.getClass());
        console.setMessageConsumer(message -> eventRepository.callEvent(new ConsoleMessageReadEvent(message)));
        //((CoreModuleManager) Discord.getInstance(ModuleManager.class)).setInjector(injector);
        final String scanPackage = "de.tdrstudios.discordsystem";
        Discord.scanEventHandler(scanPackage);
        Discord.scanCommands(scanPackage);
        Discord.scanServices(scanPackage);
        Discord.getInstance(ModuleService.class).callAction(ModuleAction.STARTED);
        eventRepository.callEvent(new ApplicationReadyEvent());
    }

    @EventHandler
    public void onReady(ApplicationReadyEvent event) {
        long timeNeeded = System.currentTimeMillis() - startTime;
        System.out.println("Started DiscordSysten in "+new DecimalFormat("0.00").format(timeNeeded/1000.0).replace(",",".")+" seconds");
    }
}
