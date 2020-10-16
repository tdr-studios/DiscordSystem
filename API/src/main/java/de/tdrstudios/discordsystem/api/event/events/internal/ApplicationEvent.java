package de.tdrstudios.discordsystem.api.event.events.internal;

import com.google.inject.Injector;
import de.tdrstudios.discordsystem.api.event.Event;
import lombok.Getter;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ApplicationEvent extends Event {
    @Getter
    private final Injector injector;

    public ApplicationEvent(Injector injector) {
        this.injector = injector;
    }
}
