package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void reload(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!sender.hasPermission("preventstabby.command.reload")) {
                PluginMessages.parseMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                return;
            }
            PreventStabby.getPlugin().reloadPluginConfig(sender);
        });
    }

}
