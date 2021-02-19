package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvp;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void reload(CommandSender sender) {

        Bukkit.getScheduler().runTaskAsynchronously(TogglePvp.getPlugin(), () -> {
            if (!sender.hasPermission("togglepvp.command.reload")) {
                String message = TogglePvp.getPlugin().getConfigCache().getNo_permission();
                BaseComponent[] component = TextComponent.fromLegacyText(message);
                sender.spigot().sendMessage(component);
                return;
            }
            TogglePvp.getPlugin().reloadPluginConfig(sender);
        });

    }

}
