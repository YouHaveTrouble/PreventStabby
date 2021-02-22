package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements TabExecutor {

    private final HashMap<String, String> subCommands = new HashMap<>();

    public MainCommand() {
        subCommands.put("help", "togglepvp.command");
        subCommands.put("toggle", "togglepvp.command.toggle");
        subCommands.put("on", "togglepvp.command.toggle");
        subCommands.put("enable", "togglepvp.command.toggle");
        subCommands.put("off", "togglepvp.command.toggle");
        subCommands.put("disable", "togglepvp.command.toggle");
        subCommands.put("reload", "togglepvp.command.reload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("togglepvp.command")) {
            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "help":
                        HelpCommand.help(sender, args);
                        break;
                    case "toggle":
                        PvpToggleCommand.toggle(sender, args);
                        break;
                    case "on":
                    case "enable":
                        PvpToggleCommand.enable(sender, args);
                        break;
                    case "off":
                    case "disable":
                        PvpToggleCommand.disable(sender, args);
                        break;
                    case "reload":
                        ReloadCommand.reload(sender);
                        break;
                    default:
                        sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getNo_such_command()));
                        break;
                }
            } else {
                HelpCommand.help(sender, args);
            }
        } else {
            sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getNo_permission()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            String arg1 = args[0].toLowerCase();
            for (Map.Entry<String, String> entry : subCommands.entrySet()) {
                if (entry.getKey().toLowerCase().startsWith(arg1) && sender.hasPermission(entry.getValue()))
                    commands.add(entry.getKey());
            }
            return commands;
        } else if (args.length == 2 && sender.hasPermission("togglepvp.command.toggle")) {
            switch (args[0].toLowerCase()) {
                default:
                    break;
                case "toggle":
                case "on":
                case "enable":
                case "off":
                case "disable":
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        commands.add(player.getName());
                    }
                    break;
            }
            return commands;
        }
        return null;
    }
}
