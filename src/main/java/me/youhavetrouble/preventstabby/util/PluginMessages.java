package me.youhavetrouble.preventstabby.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.youhavetrouble.preventstabby.PreventStabby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginMessages {

    public static String parseMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String parseMessage(CommandSender sender,String message) {

        if (sender instanceof Player && isPlaceholderApiEnabled()) {
            Player player = (Player) sender;
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static boolean isPlaceholderApiEnabled() {
        return PreventStabby.getPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static void sendMessage(CommandSender sender, String message) {
        message = parseMessage(sender, message);
        sender.sendMessage(message);
    }

    public static void sendActionBar(Player player, String message) {
        // TODO use adventure
        message = parseMessage(player, message);
        BaseComponent[] component = TextComponent.fromLegacyText(parseMessage(message));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

    public static void sendActionBar(UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        sendActionBar(player, message);
    }

    public static String parsePlayerName(Player player, String message) {
        message = message.replaceAll("%player%", player.getDisplayName());
        return parseMessage(message);
    }

    public static void broadcastMessage(Player player, String message) {
        message = parsePlayerName(player, message);
        if (PreventStabby.getPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        message = parseMessage(message);
        BaseComponent[] component = TextComponent.fromLegacyText(parseMessage(message));
        Bukkit.spigot().broadcast(component);
    }

}
