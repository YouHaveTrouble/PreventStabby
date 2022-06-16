package me.youhavetrouble.preventstabby.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.youhavetrouble.preventstabby.PreventStabby;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginMessages {

    public static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
    private static final BukkitAudiences audiences = PreventStabby.getAudiences();

    public static Component parseMessage(String message) {
        message = makeColorsWork('&', message);
        return MINIMESSAGE.deserialize(message);
    }

    public static Component parseMessage(CommandSender sender,String message) {

        if (sender instanceof Player && isPlaceholderApiEnabled()) {
            Player player = (Player) sender;
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return parseMessage(message);
    }

    private static boolean isPlaceholderApiEnabled() {
        return PreventStabby.getPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static void sendMessage(CommandSender sender, String message) {
        audiences.sender(sender).sendMessage(parseMessage(sender, message));
    }

    public static void sendActionBar(Player player, String message) {
        Component parsedMessage = parseMessage(player, message);
        audiences.player(player).sendActionBar(parsedMessage);
    }

    public static void sendActionBar(UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        sendActionBar(player, message);
    }

    public static String parsePlayerName(Player player, String message) {
        message = message.replaceAll("%player%", player.getDisplayName());
        return message;
    }

    public static void broadcastMessage(Player player, String message) {
        message = parsePlayerName(player, message);
        if (PreventStabby.getPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        audiences.all().sendMessage(parseMessage(message));
    }

    /**
     * Swaps most legacy color codes to adventure minimessage tags.
     * @param symbol Usually '&'.
     * @param string String to replace symbols in.
     * @return String with legacy color codes replaced with minimessage tags.
     */
    private static String makeColorsWork(Character symbol, String string) {
        // Adventure and ChatColor do not like each other, so this is a thing.
        string = string.replaceAll(symbol + "0", "<black>");
        string = string.replaceAll(symbol + "1", "<dark_blue>");
        string = string.replaceAll(symbol + "2", "<dark_green>");
        string = string.replaceAll(symbol + "3", "<dark_aqua>");
        string = string.replaceAll(symbol + "4", "<dark_red>");
        string = string.replaceAll(symbol + "5", "<dark_purple>");
        string = string.replaceAll(symbol + "6", "<gold>");
        string = string.replaceAll(symbol + "7", "<gray>");
        string = string.replaceAll(symbol + "8", "<dark_gray>");
        string = string.replaceAll(symbol + "9", "<blue>");
        string = string.replaceAll(symbol + "a", "<green>");
        string = string.replaceAll(symbol + "b", "<aqua>");
        string = string.replaceAll(symbol + "c", "<red>");
        string = string.replaceAll(symbol + "d", "<light_purple>");
        string = string.replaceAll(symbol + "e", "<yellow>");
        string = string.replaceAll(symbol + "f", "<white>");
        string = string.replaceAll(symbol + "k", "<obfuscated>");
        string = string.replaceAll(symbol + "l", "<bold>");
        string = string.replaceAll(symbol + "m", "<strikethrough>");
        string = string.replaceAll(symbol + "n", "<underlined>");
        string = string.replaceAll(symbol + "o", "<italic>");
        string = string.replaceAll(symbol + "r", "<reset>");
        return string;
    }

}
