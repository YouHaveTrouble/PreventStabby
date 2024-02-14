package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.Instant;
import java.util.UUID;

public class PlayerListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, new PlayerData(uuid, false));
    }

    /**
     * This event is here to save player's data to database
     * Also punishes players who log out during combat
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!PreventStabby.getPlugin().getConfigCache().punish_for_combat_logout) return;
        PlayerData playerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        if (!playerData.isInCombat()) return;
        player.setHealth(0);
        if (!PreventStabby.getPlugin().getConfigCache().punish_for_combat_logout_announce) return;
        PluginMessages.broadcastMessage(player, PreventStabby.getPlugin().getConfigCache().punish_for_combat_logout_message);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        playerData.setTeleportTimestamp(Instant.now().getEpochSecond());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        PlayerData playerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        if (playerData == null) return;
        playerData.markNotInCombat();
    }

}
