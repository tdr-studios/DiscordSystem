package de.tdrstudios.discordsystem.api.commands;

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

    static Permission guildPermission(net.dv8tion.jda.api.Permission permission) {
        return sender -> {
            if (sender instanceof GuildDiscordCommandSender) {
                return ((GuildDiscordCommandSender) sender).getMember().getPermissions().stream().anyMatch(p -> permission == p);
            }else return false;
        };
    }

    static Permission userIdPermission(long userId) {
        return sender -> {
            if (sender instanceof GuildDiscordCommandSender) {
                return ((GuildDiscordCommandSender) sender).getAuthor().getIdLong() == userId;
            } else if (sender instanceof PrivateDiscordCommandSender) {
                return ((PrivateDiscordCommandSender) sender).getAuthor().getIdLong() == userId;
            }else return sender instanceof ConsoleCommandSender;
        };
    }
}
