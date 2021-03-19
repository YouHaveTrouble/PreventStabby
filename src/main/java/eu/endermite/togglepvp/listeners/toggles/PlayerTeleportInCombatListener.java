package eu.endermite.togglepvp.listeners.toggles;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerManager;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerTeleportInCombatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleportInCombat(org.bukkit.event.player.PlayerTeleportEvent event) {

        if (!TogglePvp.getPlugin().getConfigCache().isBlock_teleports_in_combat())
            return;

        PlayerManager playerManager = TogglePvp.getPlugin().getPlayerManager();

        if (!playerManager.getPlayer(event.getPlayer().getUniqueId()).isInCombat())
            return;

        if (event.getPlayer().hasPermission("toglepvp.combatteleportblock.bypass"))
            return;

        event.setCancelled(true);
        PluginMessages.sendMessage(event.getPlayer(), TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat());

    }

}
