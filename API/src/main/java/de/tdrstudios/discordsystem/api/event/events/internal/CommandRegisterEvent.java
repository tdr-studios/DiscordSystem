package de.tdrstudios.discordsystem.api.event.events.internal;

import de.tdrstudios.discordsystem.api.commands.Command;
import de.tdrstudios.discordsystem.api.commands.CreateCommand;
import de.tdrstudios.discordsystem.api.event.Event;

public class CommandRegisterEvent extends Event {
    public Command getCommand() {
        return command;
    }

    public CreateCommand getCommandAnnotation() {
        return commandAnnotation;
    }

    private final Command command;
    private final CreateCommand commandAnnotation;

    public CommandRegisterEvent(Command command, CreateCommand commandAnnotation) {
        this.command = command;
        this.commandAnnotation = commandAnnotation;
    }
}
