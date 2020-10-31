package de.tdrstudios.discordsystem.api.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public abstract class Command {
    private List<String> aliases = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    private String description = "This is the default description";

    public String getName() {
        return name;
    }

    private String name;

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    private ExecutorType executorType;

    public void setName(String name) {
        this.name = name;
    }

    private String[] cachedAliases = {};

    public void onRegister() {}


    public void addAlias(String alias) {
        aliases.add(alias);
        updateAliasCache();
    }

    private void updateAliasCache() {
        cachedAliases = aliases.toArray(new String[aliases.size()]);
    }

    public String[] getAliases() {
        return cachedAliases;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract void execute(CommandSender sender, String[] args);
}
