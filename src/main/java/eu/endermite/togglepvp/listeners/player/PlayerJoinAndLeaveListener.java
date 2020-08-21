package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.time.Instant;
import java.util.HashMap;

@eu.endermite.togglepvp.util.Listener
public class PlayerJoinAndLeaveListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HashMap<String, Object> playerData;
        playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(player.getUniqueId());
        playerData.put("cachetime", TogglePvP.getPlugin().getPlayerManager().refreshedCacheTime());
        playerData.put("combattime", Instant.now().getEpochSecond()-1);
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
            long combatTime = (long) TogglePvP.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()).get("combattime");
            if (combatTime > now) {
                player.setHealth(0);
                if (TogglePvP.getPlugin().getConfigCache().isPunish_for_combat_logout_announce()) {
                    PluginMessages.broadcastMessage(player, TogglePvP.getPlugin().getConfigCache().getPunish_for_combat_logout_message());
                }
                TogglePvP.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()).replace("combattime", now-1);
            }
        }
    }
}
