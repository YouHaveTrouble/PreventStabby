package me.youhavetrouble.preventstabby.config;

import org.bukkit.command.CommandSender;

public enum PreventStabbyPermission {

    COMMAND("command"),
    COMMAND_TOGGLE("command.toggle"),
    COMMAND_TOGGLE_OTHERS("command.toggle.others"),
    COMMAND_RELOAD("command.reload"),
    COMMAND_GLOBAL_TOGGLE("command.toggle.global"),

    COMMAND_HELP("command.help");

    private final String permission;
    PreventStabbyPermission(String permission) {
        this.permission = "preventstabby."+permission;
    }

    public boolean doesCommandSenderHave(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    @Override
    public String toString() {
        return permission;
    }

}
