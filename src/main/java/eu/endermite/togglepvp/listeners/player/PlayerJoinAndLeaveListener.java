package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerData;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;

@eu.endermite.togglepvp.util.Listener
public class PlayerJoinAndLeaveListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
        PlayerData playerData = smartCache.getPlayerData(player.getUniqueId());
        playerData.setLoginTimestamp(Instant.now().getEpochSecond());
    }

    /**
     * This event is here to save player's data to database
     * Also punishes players who log out during combat
     */
    @EventHandler
    public void onPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TogglePvp.getPlugin().getSqLite().updatePlayerInfo(player.getUniqueId(), TogglePvp.getPlugin().getPlayerManager().getPlayer(player.getUniqueId()));
        if (!TogglePvp.getPlugin().getConfigCache().isPunish_for_combat_logout())
            return;

        SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
        long now = Instant.now().getEpochSecond();
        long combatTime = smartCache.getPlayerData(player.getUniqueId()).getCombattime();

        if (combatTime <= now)
            return;

        player.setHealth(0);
        if (TogglePvp.getPlugin().getConfigCache().isPunish_for_combat_logout_announce())
            PluginMessages.broadcastMessage(player, TogglePvp.getPlugin().getConfigCache().getPunish_for_combat_logout_message());

        PlayerData playerData = TogglePvp.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        playerData.setCombattime(now - 1);

    }
}
