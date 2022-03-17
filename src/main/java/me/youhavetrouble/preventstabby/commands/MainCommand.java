package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
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
        subCommands.put("help", "preventstabby.command");
        subCommands.put("toggle", "preventstabby.command.toggle");
        subCommands.put("on", "preventstabby.command.toggle");
        subCommands.put("enable", "preventstabby.command.toggle");
        subCommands.put("off", "preventstabby.command.toggle");
        subCommands.put("disable", "preventstabby.command.toggle");
        subCommands.put("reload", "preventstabby.command.reload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("preventstabby.command")) {
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
                        sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getNo_such_command()));
                        break;
                }
            } else {
                HelpCommand.help(sender, args);
            }
        } else {
            sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getNo_permission()));
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
        } else if (args.length == 2 && sender.hasPermission("preventstabby.command.toggle")) {
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