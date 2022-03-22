package me.youhavetrouble.preventstabby.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.youhavetrouble.preventstabby.PreventStabby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginMessages {

    public static String parseMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, String message) {
        String parsedMessage = message;
        if (PreventStabby.getPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            parsedMessage = PlaceholderAPI.setPlaceholders(player, parsedMessage);
        }
        parsedMessage = ChatColor.translateAlternateColorCodes('&', parsedMessage);
        player.sendMessage(parsedMessage);
    }

    public static void sendActionBar(Player player, String message) {
        // TODO use adventure
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
        message = parseMessage(message);
        BaseComponent[] component = TextComponent.fromLegacyText(parseMessage(message));
        Bukkit.spigot().broadcast(component);
    }

}
