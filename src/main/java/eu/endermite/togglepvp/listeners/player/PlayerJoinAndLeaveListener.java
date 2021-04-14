package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerData;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerJoinAndLeaveListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData playerData = TogglePvp.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        long time = Instant.now().getEpochSecond();
        if (playerData == null) {
            TogglePvp.getPlugin().getPlayerManager().addPlayer(uuid, new PlayerData(false));
            Bukkit.getScheduler().runTaskAsynchronously(TogglePvp.getPlugin(), () -> {
                PlayerData data = TogglePvp.getPlugin().getSqLite().getPlayerInfo(uuid);
                TogglePvp.getPlugin().getPlayerManager().addPlayer(uuid, data);
                data.setLoginTimestamp(time);
            });
            return;
        }
        playerData.refreshCachetime();
        playerData.setLoginTimestamp(time);
    }

    /**
     * This event is here to save player's data to database
     * Also punishes players who log out during combat
     */
    @EventHandler
    public void onPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(TogglePvp.getPlugin(), () -> TogglePvp.getPlugin().getSqLite().updatePlayerInfo(player.getUniqueId(), TogglePvp.getPlugin().getPlayerManager().getPlayer(player.getUniqueId())));

        if (!TogglePvp.getPlugin().getConfigCache().isPunish_for_combat_logout())
            return;

        SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
        PlayerData playerData = smartCache.getPlayerData(player.getUniqueId());

        if (!playerData.isInCombat())
            return;

        player.setHealth(0);
        if (TogglePvp.getPlugin().getConfigCache().isPunish_for_combat_logout_announce())
            PluginMessages.broadcastMessage(player, TogglePvp.getPlugin().getConfigCache().getPunish_for_combat_logout_message());


    }
}
