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
                long now = Instant.now().getEpochSecond();
                Iterator<Map.Entry<UUID, HashMap<String, Object>>> it = TogglePvP.getPlugin().getPlayerManager().getPlayerList().entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry<UUID,HashMap<String, Object>> cacheEntry = it.next();
                    if ((Long) cacheEntry.getValue().get("cachetime") < now) {
                        TogglePvP.getPlugin().getPlayerManager().removePlayer(cacheEntry.getKey());
                    }
                }
            }
        }.runTaskTimerAsynchronously(TogglePvP.getPlugin(), 100, 100);
    }


}
