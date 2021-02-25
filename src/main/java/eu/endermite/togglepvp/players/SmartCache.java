package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class SmartCache {

    public void runSmartCache() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(TogglePvp.getPlugin(), () -> {
            // Refresh cache timer if player is online
            for (Map.Entry<UUID, PlayerData> e : TogglePvp.getPlugin().getPlayerManager().getPlayerList().entrySet()) {
                try {
                    Player player = Bukkit.getPlayer(e.getKey());
                    if (player != null && player.isOnline()) {
                        TogglePvp.getPlugin().getPlayerManager().refreshPlayersCacheTime(e.getKey());
                    }
                } catch (NullPointerException ignored) {}
            }
            // Check for entries that should be invalidated
            try {
                long now = Instant.now().getEpochSecond();
                TogglePvp.getPlugin().getPlayerManager().getPlayerList().entrySet()
                        .removeIf(cacheEntry -> cacheEntry.getValue().getCachetime() < now);
            } catch (Exception ignored) {}
        }, 100, 100);
    }

    public PlayerData getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        try {
            TogglePvp.getPlugin().getPlayerManager().refreshPlayersCacheTime(uuid);
            return TogglePvp.getPlugin().getPlayerManager().getPlayer(uuid);
        } catch (NullPointerException e) {
            // If player data is not in cache get it from database and put into cache
            try {
                PlayerData playerData = TogglePvp.getPlugin().getSqLite().getPlayerInfo(uuid);
                TogglePvp.getPlugin().getPlayerManager().addPlayer(uuid, playerData);
                return playerData;
            } catch (NullPointerException ex) {
                // Return false if database call fails
                return new PlayerData(false);
            }
        }
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        // If player is in cache update that
        if (TogglePvp.getPlugin().getPlayerManager().getPlayer(uuid) != null) {
            TogglePvp.getPlugin().getPlayerManager().getPlayer(uuid).setPvpEnabled(state);
        }
        // Update the database aswell
        TogglePvp.getPlugin().getSqLite().updatePlayerInfo(uuid, new PlayerData(state));
    }


}
