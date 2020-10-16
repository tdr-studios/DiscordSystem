package de.tdrstudios.discordsystem.example.commands;

import de.tdrstudios.discordsystem.api.commands.Command;
import de.tdrstudios.discordsystem.api.commands.CommandSender;
import de.tdrstudios.discordsystem.api.commands.CreateCommand;
import de.tdrstudios.discordsystem.api.commands.ExecutorType;

@CreateCommand(name = "example", executorType = ExecutorType.ALL)
public class TestCommand extends Command {

    @Override
    public void onRegister() {
        addAlias("examplealias");
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("This is an alias");
    }
}
