package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.event.Event;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.services.CreateService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService
public class CoreEventService implements EventService {
    private final Injector injector;
    private HashMap<Class<?>, List<Method>> methodRegistry = new HashMap<>();
    private HashMap<Object, List<Method>> listeners = new HashMap<>();

    @Inject
    public CoreEventService(Injector injector) {
        this.injector = injector;
    }

    public void callEvent(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        methodRegistry.forEach((clazz, methods) -> {
            for (Method method : methods) {
                if (method.getParameterTypes()[0].getName().equals(eventClass.getName())) {
                    try {
                        Object object = injector.getInstance(clazz);
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listeners.forEach((o, methods) -> {
            for (Method method : methods) {
                if (method.getParameterTypes()[0].getName().equals(eventClass.getName())) {
                    try {
                        method.invoke(o, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void callEvent(net.dv8tion.jda.api.events.Event event) {
        Class<? extends net.dv8tion.jda.api.events.Event> eventClass = event.getClass();
        methodRegistry.forEach((clazz, methods) -> {
            for (Method method : methods) {
                if (method.getParameterTypes()[0].getName().equals(eventClass.getName())) {
                    try {
                        Object object = injector.getInstance(clazz);
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listeners.forEach((o, methods) -> {
            for (Method method : methods) {
                if (method.getParameterTypes()[0].getName().equals(eventClass.getName())) {
                    try {
                        method.invoke(o, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addListener(Object object) {
        Class<?> clazz = object.getClass();
        List<Method> methods = listeners.get(object) == null ? new ArrayList<>() : listeners.get(object);
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            if (method.getParameterCount() == 1) {
                methods.add(method);
            }
        }
        if (methods.size() != 0)
            listeners.put(object, methods);
    }

    public void removeListener(Object object) {
        listeners.remove(object);
    }

    public void scanForEvents(Class<?> clazz) {
        List<Method> methods = methodRegistry.get(clazz) == null ? new ArrayList<>() : methodRegistry.get(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            if (method.getParameterCount() == 1) {
                methods.add(method);
            }
        }
        if (methods.size() != 0)
            methodRegistry.put(clazz, methods);
    }

    @Override
    public void initialize() throws Exception {
        Discord.getInstance(ModuleService.class).callAction(ModuleAction.EVENT_READY);
    }
}
