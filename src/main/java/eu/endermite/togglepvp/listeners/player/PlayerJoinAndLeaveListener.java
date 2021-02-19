package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.players.PlayerData;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.time.Instant;

@eu.endermite.togglepvp.util.Listener
public class PlayerJoinAndLeaveListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(player.getUniqueId());
        TogglePvP.getPlugin().getPlayerManager().addPlayer(player.getUniqueId(), playerData);
    }
    /**
     * This event is here to dump player's saved options from memory
     * Also punishes players who log out during combat
     */
    @EventHandler
    public void onPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TogglePvP.getPlugin().getSqLite().updatePlayerInfo(player.getUniqueId(), TogglePvP.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()));
        if (TogglePvP.getPlugin().getConfigCache().isPunish_for_combat_logout()) {
            long now = Instant.now().getEpochSecond();
            long combatTime = TogglePvP.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()).getCombattime();
            if (combatTime > now) {
                player.setHealth(0);
                if (TogglePvP.getPlugin().getConfigCache().isPunish_for_combat_logout_announce()) {
                    PluginMessages.broadcastMessage(player, TogglePvP.getPlugin().getConfigCache().getPunish_for_combat_logout_message());
                }
                TogglePvP.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()).setCombattime(now-1);
            }
        }
    }
}
