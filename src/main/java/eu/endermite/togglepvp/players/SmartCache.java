package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class SmartCache {

    public static void runSmartCache() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(TogglePvP.getPlugin(), () -> {
            // Refresh cache timer if player is online
            for (Map.Entry<UUID, PlayerData> e : TogglePvP.getPlugin().getPlayerManager().getPlayerList().entrySet()) {
                try {
                    Player player = Bukkit.getPlayer(e.getKey());
                    if (player != null && player.isOnline()) {
                        TogglePvP.getPlugin().getPlayerManager().refreshPlayersCacheTime(e.getKey());
                    }
                } catch (NullPointerException ignored) {}
            }
            // Check for entries that should be invalidated
            try {
                long now = Instant.now().getEpochSecond();
                TogglePvP.getPlugin().getPlayerManager().getPlayerList().entrySet().removeIf(cacheEntry -> cacheEntry.getValue().getCachetime() < now);
            } catch (Exception ignored) {}
        }, 100, 100);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        try {
            TogglePvP.getPlugin().getPlayerManager().refreshPlayersCacheTime(uuid);
            return TogglePvP.getPlugin().getPlayerManager().getPlayer(uuid);
        } catch (NullPointerException e) {
            // If player data is not in cache get it from database and put into cache
            try {
                PlayerData playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(uuid);
                TogglePvP.getPlugin().getPlayerManager().addPlayer(uuid, playerData);
                return playerData;
            } catch (NullPointerException ex) {
                // Return false if database call fails
                return new PlayerData(false);
            }
        }
    }


}
