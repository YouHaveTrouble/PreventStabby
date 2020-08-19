package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.util.PluginMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand {

    public static void toggle(CommandSender sender, String[] args) {

        Bukkit.getScheduler().runTaskAsynchronously(TogglePvP.getPlugin(), () -> {

            if (!sender.hasPermission("togglepvp.command.toggle")) {
                String message = TogglePvP.getPlugin().getConfigCache().getNo_permission();
                BaseComponent[] component = TextComponent.fromLegacyText(message);
                sender.spigot().sendMessage(component);
                return;
            }

            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean currentState = TogglePvP.getPlugin().getPlayerManager().togglePlayerPvpState(player);

                    String message = "";
                    if (currentState) {
                        message = PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getPvp_enabled());
                    } else {
                        message = PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getPvp_disabled());
                    }
                    BaseComponent[] component = TextComponent.fromLegacyText(message);
                    player.spigot().sendMessage(ChatMessageType.CHAT, component);
                } else {
                    sender.sendMessage("Try /pvp toggle <player>");
                }
            }

        });
    }

    public static void enable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("togglepvp.command.toggle")) {
            String message = TogglePvP.getPlugin().getConfigCache().getNo_permission();
            BaseComponent[] component = TextComponent.fromLegacyText(message);
            sender.spigot().sendMessage(component);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                TogglePvP.getPlugin().getPlayerManager().setPlayerPvpState(player, true);
                String message = PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getPvp_enabled());
                BaseComponent[] component = TextComponent.fromLegacyText(message);
                player.spigot().sendMessage(ChatMessageType.CHAT, component);
            } else {
                sender.sendMessage("Try /pvp enable <player>");
            }
        }
    }

    public static void disable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("togglepvp.command.toggle")) {
            String message = TogglePvP.getPlugin().getConfigCache().getNo_permission();
            BaseComponent[] component = TextComponent.fromLegacyText(message);
            sender.spigot().sendMessage(component);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                TogglePvP.getPlugin().getPlayerManager().setPlayerPvpState(player, false);
                String message = PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getPvp_disabled());
                BaseComponent[] component = TextComponent.fromLegacyText(message);
                player.spigot().sendMessage(ChatMessageType.CHAT, component);
            } else {
                sender.sendMessage("Try /pvp disable <player>");
            }
        }
    }

}
