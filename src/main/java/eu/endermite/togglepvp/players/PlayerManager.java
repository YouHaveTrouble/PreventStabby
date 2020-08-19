package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager {

    HashMap<Player, HashMap<String, Object>> playerList = new HashMap<>();

    public PlayerManager() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            HashMap<String, Object> playerData;
            playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(p);
            playerList.put(p, playerData);
        }
    }

    public HashMap<String, Object> getPlayer(Player p) {
        return playerList.get(p);
    }

    public void addPlayer(Player player, HashMap<String,Object> data) {
        playerList.put(player, data);
    }

    public void removePlayer(Player player) {
        playerList.remove(player);
    }

    public boolean getPlayerPvPState(Player p) {
        return (boolean) playerList.get(p).get("pvpenabled");
    }

    public void setPlayerPvpState(Player player, boolean state) {
        playerList.get(player).replace("pvpenabled", state);
    }

    public boolean togglePlayerPvpState(Player player) {
        boolean currentState = (boolean) playerList.get(player).get("pvpenabled");
        if (currentState) {
            playerList.get(player).replace("pvpenabled", false);
            return false;
        } else {
            playerList.get(player).replace("pvpenabled", true);
            return true;
        }
    }
}
