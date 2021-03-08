package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerData;
import eu.endermite.togglepvp.players.SmartCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.time.Instant;

@eu.endermite.togglepvp.util.Listener
public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(org.bukkit.event.player.PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
        PlayerData playerData = smartCache.getPlayerData(player.getUniqueId());
        playerData.setTeleportTimestamp(Instant.now().getEpochSecond());
    }

}
