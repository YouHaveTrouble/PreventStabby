package eu.endermite.togglepvp.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class PluginMessages {

    public static String parseMessage(String message) {
        //TODO PAPI support
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendActionBar(Player player, String message) {
        BaseComponent[] component = TextComponent.fromLegacyText(parseMessage(message));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

}
