package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void reload(CommandSender sender) {

        Bukkit.getScheduler().runTaskAsynchronously(TogglePvp.getPlugin(), () -> {
            if (!sender.hasPermission("togglepvp.command.reload")) {
                String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }
            TogglePvp.getPlugin().reloadPluginConfig(sender);
        });

    }

}
