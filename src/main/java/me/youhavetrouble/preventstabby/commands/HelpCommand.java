package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.PreventStabbyPermission;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void help(CommandSender sender, String[] args) {
        Bukkit.getAsyncScheduler().runNow(PreventStabby.getPlugin(), (task) -> {

            if (!PreventStabbyPermission.COMMAND_HELP.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
                return;
            }

            Component helpComponent = PluginMessages.MINIMESSAGE
                    .deserialize("<aqua><strikethrough>                    </strikethrough> PreventStabby Help <strikethrough>                    </strikethrough>")
                    .append(Component.newline())
                    .append(PluginMessages.MINIMESSAGE.deserialize("<blue>/pvp <aqua><bold>help</bold> <white>- shows this message"));

            if (PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
                helpComponent = helpComponent.append(Component.newline());
                helpComponent = helpComponent
                        .append(PluginMessages.MINIMESSAGE.deserialize("<blue>/pvp <aqua><bold>[on/off]</bold> <white>- enables or disables PvP"))
                        .append(Component.newline())
                        .append(PluginMessages.MINIMESSAGE.deserialize("<blue>/pvp <aqua><bold>toggle</bold> <white>- toggles PvP status"));
            }
            sender.sendMessage(helpComponent);
        });
    }

}
