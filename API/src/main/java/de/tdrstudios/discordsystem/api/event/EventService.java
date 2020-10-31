package de.tdrstudios.discordsystem.api.event;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
