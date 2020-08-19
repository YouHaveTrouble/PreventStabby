package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand {

    public static void help(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(TogglePvP.getPlugin(), () -> {

            List<String> helpPage = new ArrayList<>();

            helpPage.add(PluginMessages.parseMessage("&b&m        &bTogglePvP Help&b&m        "));
            helpPage.add(PluginMessages.parseMessage("&9/pvp &b&lhelp &f- shows this message"));
            if (sender.hasPermission("togglepvp.command.toggle")) {
                helpPage.add(PluginMessages.parseMessage("&9/pvp &b&l[on/off] &f- enables or disables PvP"));
                helpPage.add(PluginMessages.parseMessage("&9/pvp &b&ltoggle &f- toggles PvP status"));
            }
            String[] helpReady = helpPage.toArray(new String[0]);
            sender.sendMessage(helpReady);

        });
    }

}
