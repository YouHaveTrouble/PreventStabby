package me.youhavetrouble.preventstabby.config;

import org.bukkit.command.CommandSender;

public enum PreventStabbyPermission {

    COMMAND("preventstabby.command"),
    COMMAND_TOGGLE("preventstabby.command.toggle"),
    COMMAND_TOGGLE_OTHERS("preventstabby.command.toggle.others"),
    COMMAND_RELOAD("preventstabby.command.reload");

    private final String permission;
    PreventStabbyPermission(String permission) {
        this.permission = permission;
    }

    public boolean doesCommandSenderHave(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    @Override
    public String toString() {
        return permission;
    }

}
