package de.tdrstudios.discordsystem.api.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public abstract class Command {
    private List<String> aliases = new ArrayList<>();
    private String description;

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
