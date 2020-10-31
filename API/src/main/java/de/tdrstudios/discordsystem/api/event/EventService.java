package de.tdrstudios.discordsystem.api.event;

import de.tdrstudios.discordsystem.api.services.Service;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface EventService extends Service {

    void callEvent(Event event);

    void callEvent(net.dv8tion.jda.api.events.Event event);

    void addListener(Object object);

    void removeListener(Object object);

    void scanForEvents(Class<?> clazz);

}
