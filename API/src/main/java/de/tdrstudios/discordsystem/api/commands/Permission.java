package de.tdrstudios.discordsystem.api.commands;

import de.dseelp.database.api.storage.StringStorage;

public interface Permission {
    boolean check(CommandSender sender);

    static Permission guildOwnerPermission() {
        return sender -> {
          if (sender instanceof GuildDiscordCommandSender) {
              return ((GuildDiscordCommandSender) sender).getMember().isOwner();
          }else return false;
        };
    }

    static Permission guildRolePermission(String role) {
        return sender -> {
            if (sender instanceof GuildDiscordCommandSender) {
                return ((GuildDiscordCommandSender) sender).getMember().getRoles().stream().anyMatch(r -> r.getName().equals(role));
            }else return false;
        };
    }
}
