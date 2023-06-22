package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class SmartCache {

    public void runSmartCache() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(PreventStabby.getPlugin(), () -> {
            // Refresh cache timer if player is online
            for (Map.Entry<UUID, PlayerData> e : PreventStabby.getPlugin().getPlayerManager().getPlayerList().entrySet()) {
                if (e == null) continue;
                Player player = Bukkit.getPlayer(e.getKey());
                if (player != null && player.isOnline()) {
                    e.getValue().refreshCacheTime();
                }
            }
            // Check for entries that should be invalidated
            try {
                long now = Instant.now().getEpochSecond();
                PreventStabby.getPlugin().getPlayerManager().getPlayerList().entrySet()
                        .removeIf(cacheEntry -> cacheEntry.getValue().getCachetime() < now);
            } catch (Exception ignored) {
            }
        }, 100, 100);
    }

    public PlayerData getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        PlayerData data = PreventStabby.getPlugin().getPlayerManager().getPlayer(uuid);
        if (data != null) {
            data.refreshCacheTime();
            return data;
        }
        PlayerData playerData = PreventStabby.getPlugin().getSqLite().getPlayerInfo(uuid);
        if (playerData == null) {
            playerData = new PlayerData(uuid, false);
        }
        PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, playerData);
        return playerData;
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        // If player is in cache update that
        if (PreventStabby.getPlugin().getPlayerManager().getPlayer(uuid) != null) {
            PreventStabby.getPlugin().getPlayerManager().getPlayer(uuid).setPvpEnabled(state);
        }
        // Update the database aswell
        PreventStabby.getPlugin().getSqLite().updatePlayerInfo(uuid, new PlayerData(uuid, state));
    }


}
