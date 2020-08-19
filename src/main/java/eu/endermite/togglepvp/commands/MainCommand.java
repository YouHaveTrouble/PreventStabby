package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {

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
                        sender.sendMessage(PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getNo_such_command()));
                        break;
                }
            } else {
                HelpCommand.help(sender, args);
            }
        } else {
            sender.sendMessage(PluginMessages.parseMessage(TogglePvP.getPlugin().getConfigCache().getNo_permission()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        String arg1 = args[0].toLowerCase();

        List<String> noPerm = new ArrayList<>();
        noPerm.add("help");

        List<String> reloadPerm = new ArrayList<>();
        reloadPerm.add("reload");

        List<String> togglePerm = new ArrayList<>();
        togglePerm.add("toggle");
        togglePerm.add("on");
        togglePerm.add("enable");
        togglePerm.add("off");
        togglePerm.add("disable");

        if (args.length == 1) {
            for (String noPermCmd : noPerm) {
                if (noPermCmd.startsWith(arg1))
                    commands.add(noPermCmd);
            }
            if (sender.hasPermission("togglepvp.command.toggle")) {
                for (String togglePermCmd : togglePerm) {
                    if (togglePermCmd.startsWith(arg1))
                        commands.add(togglePermCmd);
                }
            }
            if (sender.hasPermission("togglepvp.command.reload")) {
                for (String reloadPermCmd : reloadPerm) {
                    if (reloadPermCmd.startsWith(arg1))
                        commands.add(reloadPermCmd);
                }
            }
        }
        return commands;
    }
}
