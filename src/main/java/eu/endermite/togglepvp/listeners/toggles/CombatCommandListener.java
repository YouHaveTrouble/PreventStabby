package eu.endermite.togglepvp.listeners.toggles;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerManager;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class CombatCommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandInCombat(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {

        if (!TogglePvp.getPlugin().getConfigCache().isBlock_commands_in_combat())
            return;

        PlayerManager playerManager = TogglePvp.getPlugin().getPlayerManager();

        if (!playerManager.getPlayer(event.getPlayer().getUniqueId()).isInCombat())
            return;

        String command = event.getMessage().replaceFirst("/", "");

        if (!TogglePvp.getPlugin().getConfigCache().getCombatBlockedCommands().contains(command))
            return;

        if (event.getPlayer().hasPermission("toglepvp.combatcommandblock.bypass"))
            return;

        event.setCancelled(true);
        PluginMessages.sendMessage(event.getPlayer(), TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat());

    }

}
