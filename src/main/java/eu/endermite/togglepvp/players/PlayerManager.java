package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    @Getter HashMap<UUID, HashMap<String, Object>> playerList = new HashMap<>();

    public PlayerManager() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            HashMap<String, Object> playerData;
            playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(p.getUniqueId());
            playerData.put("cachetime", refreshedCacheTime());
            playerList.put(p.getUniqueId(), playerData);
        }
    }


    public long refreshedCacheTime() {
        long cacheTime = TogglePvP.getPlugin().getConfigCache().getCache_time();
        return Instant.now().getEpochSecond()+cacheTime;
    }

    public void refreshPlayersCacheTime(UUID uuid) {
        playerList.get(uuid).replace("cachetime", refreshedCacheTime());
    }

    public HashMap<String, Object> getPlayer(UUID uuid) {
        return playerList.get(uuid);
    }

    public void addPlayer(UUID uuid, HashMap<String,Object> data) {
        playerList.put(uuid, data);
    }

    public void removePlayer(UUID uuid) {
        playerList.remove(uuid);
    }

    public boolean getPlayerPvPState(UUID uuid) {
        return (boolean) playerList.get(uuid).get("pvpenabled");
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        playerList.get(uuid).replace("pvpenabled", state);
    }

    public boolean togglePlayerPvpState(UUID uuid) {
        boolean currentState = (boolean) playerList.get(uuid).get("pvpenabled");
        if (currentState) {
            playerList.get(uuid).replace("pvpenabled", false);
            return false;
        } else {
            playerList.get(uuid).replace("pvpenabled", true);
            return true;
        }
    }
}
