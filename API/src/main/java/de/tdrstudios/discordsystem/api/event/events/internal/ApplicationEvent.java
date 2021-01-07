package de.tdrstudios.discordsystem.api.event.events.internal;

import com.google.inject.Injector;
import de.tdrstudios.discordsystem.api.event.Event;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ApplicationEvent extends Event {
    private final Injector injector;

    public ApplicationEvent(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return this.injector;
    }
}
