package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.PreventStabbyPermission;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class GlobalToggleCommand {
    public static void globalToggle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!PreventStabbyPermission.COMMAND_GLOBAL_TOGGLE.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                return;
            }

            if (args.length != 2) {
                PluginMessages.sendMessage(sender, "Try /pvp override <enabled/disabled/none>");
                return;
            }

            PvpState pvpState = PvpState.valueOf(args[1].toUpperCase(Locale.ROOT));

            switch (pvpState) {
                case ENABLED:
                    PreventStabby.getPlugin().getPlayerManager().setForcedPvpState(PvpState.ENABLED);
                    PluginMessages.broadcastMessage(PreventStabby.getPlugin().getConfigCache().getForce_pvp_on());
                    break;
                case DISABLED:
                    PreventStabby.getPlugin().getPlayerManager().setForcedPvpState(PvpState.DISABLED);
                    PluginMessages.broadcastMessage(PreventStabby.getPlugin().getConfigCache().getForce_pvp_off());
                    break;
                case NONE:
                    PreventStabby.getPlugin().getPlayerManager().setForcedPvpState(PvpState.NONE);
                    PluginMessages.broadcastMessage(PreventStabby.getPlugin().getConfigCache().getForce_pvp_none());
                    break;
                default:
                    PluginMessages.sendMessage(sender, "Try /pvp override <enabled/disabled/none>");
            }

        });

    }

}
