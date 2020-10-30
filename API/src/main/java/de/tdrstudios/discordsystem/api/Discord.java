package de.tdrstudios.discordsystem.api;

import com.google.inject.Injector;
import de.tdrstudios.discordsystem.api.commands.CommandService;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.event.HandleEvents;
import de.tdrstudios.discordsystem.api.services.ComparableService;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;
import de.tdrstudios.discordsystem.utils.ClassCriteria;
import de.tdrstudios.discordsystem.utils.JsonConfig;
import de.tdrstudios.discordsystem.utils.JsonDocument;
import de.tdrstudios.discordsystem.utils.ReflectionUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateService(Integer.MAX_VALUE)
public class Discord implements Service {
    public static void setInjector(Injector injector) {
        if (Discord.injector != null) throw new UnsupportedOperationException("Discord.injector already has a value!");
        Discord.injector = injector;
    }

    @Getter
    private static Injector injector;

    public static <T> T getInstance(Class<T> clazz) {
        T instance = injector.getInstance(clazz);
        injector.injectMembers(instance);
        return instance;
    }

    private static JsonConfig config;

    public static JsonConfig getConfig() {
        return config;
    }

    @Override
    public void initialize() throws IOException {
        File file = new File("config.json");
        if (!file.exists()) file.createNewFile();
        config = JsonConfig.load(file);
        config.addDefault("bot.token", "Enter token here!");
        config.addDefault("bot.activity.activity", "https://github.com/tdr-studios");
        config.addDefault("bot.activity.action", "DEFAULT");
        config.addDefault("bot.activity.status", "ONLINE");
        config.addDefaultNumberList("adminIds", new ArrayList<>());
        config.addDefault("prefix", "+");
        config.save();
    }

    public static void scanServices(String... packageString) {
        System.out.println("Scanning "+ Arrays.toString(packageString) +" for Services...");
        EventService eventService = injector.getInstance(EventService.class);
        List<Service> services = new ArrayList<>();
        int count = 0;
        for (Class<?> clazz : ReflectionUtils.filter(packageString, ClassCriteria.annotatedWith(CreateService.class), ClassCriteria.subclassOf(Service.class))) {
            services.add((Service) getInstance(clazz));
            eventService.scanForEvents(clazz);
            count++;
        }
        System.out.println("Found "+count+" Services in "+ Arrays.toString(packageString) +"!");
        for (int i = 0; i < services.size(); i++) {

        }
        for (Service service : ComparableService.sort(services)) {
            try {
                service.initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void scanEventHandler(String... packageString) {
        System.out.println("Scanning "+ Arrays.toString(packageString) +" for EventHandlers...");
        EventService eventService = injector.getInstance(EventService.class);
        int count = 0;
        for (Class<?> clazz : ReflectionUtils.filter(packageString, ClassCriteria.annotatedWith(HandleEvents.class))) {
            eventService.scanForEvents(clazz);
            count++;
        }
        System.out.println("Found "+count+" EventHandlers in "+ Arrays.toString(packageString) +"!");
    }

    public static void scanCommands(String packageString) {
        getInstance(CommandService.class).scanCommands(packageString);
    }

    public static String getVersion() {
        return getInstance(DiscordImpl.class).getVersion();
    }

    public static String getVersionNickname() {
        return getInstance(DiscordImpl.class).getVersionNickname();
    }

    public static void shutdown() {
        getInstance(DiscordImpl.class).shutdown();
    }

    public static interface DiscordImpl extends Service {
        void shutdown();
        String getVersion();
        String getVersionNickname();
    }

    public static String getDefaultPrefix() {
        return config.getString("prefix");
    }

    public static String getPrefix(long guildId) {
        JsonDocument guildData = Discord.getInstance(DataService.class).getGuildData(guildId);
        if (guildData.has("prefix")) {
            return guildData.getString("prefix");
        }else return getDefaultPrefix();
    }

}
