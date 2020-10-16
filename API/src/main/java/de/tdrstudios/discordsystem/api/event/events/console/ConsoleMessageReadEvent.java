package de.tdrstudios.discordsystem.api.event.events.console;

import de.tdrstudios.discordsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
public class ConsoleMessageReadEvent extends Event {
    @Getter
    private final String message;
}
