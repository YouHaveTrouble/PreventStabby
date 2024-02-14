package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.PreventStabbyPermission;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements TabExecutor {

    private final HashMap<String, PreventStabbyPermission> subCommands = new HashMap<>();

    public MainCommand() {
        subCommands.put("help", PreventStabbyPermission.COMMAND_HELP);
        subCommands.put("toggle", PreventStabbyPermission.COMMAND_TOGGLE);
        subCommands.put("on", PreventStabbyPermission.COMMAND_TOGGLE);
        subCommands.put("enable", PreventStabbyPermission.COMMAND_TOGGLE);
        subCommands.put("off", PreventStabbyPermission.COMMAND_TOGGLE);
        subCommands.put("disable", PreventStabbyPermission.COMMAND_TOGGLE);
        subCommands.put("reload", PreventStabbyPermission.COMMAND_RELOAD);
        subCommands.put("override", PreventStabbyPermission.COMMAND_GLOBAL_TOGGLE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("preventstabby.command")) {
            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
            return true;
        }
        if (args.length == 0) {
            PvpToggleCommand.toggle(sender, args);
            return true;
        }

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
            case "override":
                GlobalToggleCommand.globalToggle(sender, args);
                break;
            default:
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_such_command);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            String arg1 = args[0].toLowerCase();
            for (Map.Entry<String, PreventStabbyPermission> entry : subCommands.entrySet()) {
                if (entry.getKey().toLowerCase().startsWith(arg1)
                        && entry.getValue().doesCommandSenderHave(sender))
                    commands.add(entry.getKey());
            }
            return commands;
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                default:
                    break;
                case "toggle":
                case "on":
                case "enable":
                case "off":
                case "disable":
                    if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) break;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        commands.add(player.getName());
                    }
                    break;
                case "override":
                    if (!PreventStabbyPermission.COMMAND_GLOBAL_TOGGLE.doesCommandSenderHave(sender)) break;
                    commands.add("enabled");
                    commands.add("disabled");
                    commands.add("none");
                    break;
            }
            return StringUtil.copyPartialMatches(args[1], commands, new ArrayList<>());
        }
        return null;
    }
}
