package de.tdrstudios.discordsystem.api.event.events.internal;

import com.google.inject.Injector;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ApplicationReadyEvent extends ApplicationEvent{

    public ApplicationReadyEvent(Injector injector) {
        super(injector);
    }
}
