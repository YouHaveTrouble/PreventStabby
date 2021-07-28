package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.time.Instant;

@PreventStabbyListener
public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(org.bukkit.event.player.PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        PlayerData playerData = smartCache.getPlayerData(player.getUniqueId());
        playerData.setTeleportTimestamp(Instant.now().getEpochSecond());
    }

}
