package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void reload(CommandSender sender) {

        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!sender.hasPermission("preventstabby.command.reload")) {
                String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }
            PreventStabby.getPlugin().reloadPluginConfig(sender);
        });

    }

}
