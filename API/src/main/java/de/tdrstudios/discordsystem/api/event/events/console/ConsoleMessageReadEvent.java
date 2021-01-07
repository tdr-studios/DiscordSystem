package de.tdrstudios.discordsystem.api.event.events.console;

import de.tdrstudios.discordsystem.api.event.Event;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ConsoleMessageReadEvent extends Event {
    private final String message;

    public ConsoleMessageReadEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
