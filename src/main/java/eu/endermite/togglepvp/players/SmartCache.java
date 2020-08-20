package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SmartCache {

    public static void runSmartCache() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Refresh cache timer if player is online
                for (Map.Entry<UUID, HashMap<String, Object>> e : TogglePvP.getPlugin().getPlayerManager().getPlayerList().entrySet()) {
                    try {
                        Player player = Bukkit.getPlayer(e.getKey());
                        if (player.isOnline()) {
                            TogglePvP.getPlugin().getPlayerManager().refreshPlayersCacheTime(e.getKey());
                        }
                    } catch (NullPointerException ignored) {}
                }

                // Check for entries that should be invalidated
                try {
                    long now = Instant.now().getEpochSecond();
                    TogglePvP.getPlugin().getPlayerManager().getPlayerList().entrySet().removeIf(cacheEntry -> (Long) cacheEntry.getValue().get("cachetime") < now);
                } catch (Exception ignored) {}
            }
        }.runTaskTimerAsynchronously(TogglePvP.getPlugin(), 100, 100);
    }

    public static HashMap<String, Object> getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        try {
            TogglePvP.getPlugin().getPlayerManager().refreshPlayersCacheTime(uuid);
            return TogglePvP.getPlugin().getPlayerManager().getPlayer(uuid);
        } catch (NullPointerException e) {
            // If player data is not in cache get it from database and put into cache
            try {
                HashMap<String, Object> playerData;
                playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(uuid);
                playerData.put("cachetime", TogglePvP.getPlugin().getPlayerManager().refreshedCacheTime());
                TogglePvP.getPlugin().getPlayerManager().addPlayer(uuid, playerData);
                return playerData;
            } catch (NullPointerException ex) {
                // Return null if database call fails
                return null;
            }
        }
    }


}
