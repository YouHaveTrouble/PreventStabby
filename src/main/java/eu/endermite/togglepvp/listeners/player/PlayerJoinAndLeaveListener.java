package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class PlayerJoinAndLeaveListener implements Listener {

    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        Player player = event.getPlayer();
        HashMap<String, Object> playerData;
        playerData = TogglePvP.getPlugin().getSqLite().getPlayerInfo(player);
        TogglePvP.getPlugin().getPlayerManager().addPlayer(player, playerData);

    }
    /**
     * This event is here to dump player's saved options from memory
     */
    @EventHandler
    public void onPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TogglePvP.getPlugin().getSqLite().updatePlayerInfo(player, TogglePvP.getPlugin().getPlayerManager().getPlayer(player));
        TogglePvP.getPlugin().getPlayerManager().removePlayer(player);
    }

}
