package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    @Getter HashMap<UUID, PlayerData> playerList = new HashMap<>();

    public PlayerManager() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(p.getUniqueId());
            playerList.put(p.getUniqueId(), playerData);
        }
    }


    public long refreshedCacheTime() {
        long cacheTime = TogglePvP.getPlugin().getConfigCache().getCache_time();
        return Instant.now().getEpochSecond()+cacheTime;
    }

    public long refreshedCombatTime() {
        long combatTime = TogglePvP.getPlugin().getConfigCache().getCombat_time();
        return Instant.now().getEpochSecond()+combatTime;
    }

    public void refreshPlayersCacheTime(UUID uuid) {
        playerList.get(uuid).refreshCachetime();
    }

    public void refreshPlayersCombatTime(UUID uuid) {
        try {
            playerList.get(uuid).refreshCombatTime();
        } catch (Exception ignored) {}
    }

    public PlayerData getPlayer(UUID uuid) {
        return playerList.get(uuid);
    }

    public void addPlayer(UUID uuid, PlayerData data) {
        playerList.put(uuid, data);
    }

    public void removePlayer(UUID uuid) {
        playerList.remove(uuid);
    }

    public boolean getPlayerPvPState(UUID uuid) {
        return playerList.get(uuid).isPvpEnabled();
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        playerList.get(uuid).setPvpEnabled(state);
    }

    public boolean togglePlayerPvpState(UUID uuid) {
        boolean currentState = (boolean) playerList.get(uuid).isPvpEnabled();
        if (currentState) {
            playerList.get(uuid).setPvpEnabled(false);
            return false;
        } else {
            playerList.get(uuid).setPvpEnabled(true);
            return true;
        }
    }
}
