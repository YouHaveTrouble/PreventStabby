package me.youhavetrouble.preventstabby.listeners.toggles;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class CombatCommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandInCombat(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {

        if (!PreventStabby.getPlugin().getConfigCache().isBlock_commands_in_combat())
            return;

        if (event.getPlayer().hasPermission("toglepvp.combatcommandblock.bypass"))
            return;

        PlayerManager playerManager = PreventStabby.getPlugin().getPlayerManager();

        if (!playerManager.getPlayer(event.getPlayer().getUniqueId()).isInCombat())
            return;

        String command = event.getMessage().replaceFirst("/", "");

        if (!PreventStabby.getPlugin().getConfigCache().getCombatBlockedCommands().contains(command))
            return;

        event.setCancelled(true);
        PluginMessages.sendMessage(event.getPlayer(), PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());

    }

}
