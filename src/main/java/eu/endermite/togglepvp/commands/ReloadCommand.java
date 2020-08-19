package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvP;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void reload(CommandSender sender) {

        Bukkit.getScheduler().runTaskAsynchronously(TogglePvP.getPlugin(), () -> {
            if (!sender.hasPermission("togglepvp.command.reload")) {
                String message = TogglePvP.getPlugin().getConfigCache().getNo_permission();
                BaseComponent[] component = TextComponent.fromLegacyText(message);
                sender.spigot().sendMessage(component);
                return;
            }
            TogglePvP.getPlugin().reloadPluginConfig(sender);
        });

    }

}
