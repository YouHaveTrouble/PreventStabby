package me.youhavetrouble.preventstabby.data;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) return;
        UUID uuid = event.getUniqueId();
        try {
            PreventStabby.getPlugin().getPlayerManager().getPlayerData(uuid).get();
        } catch (ExecutionException | InterruptedException e) {
            PreventStabby.getPlugin().getLogger().severe(e.getMessage());
            PreventStabby.getPlugin().getLogger().severe("Failed to load data for player %s".formatted(event.getPlayerProfile().getName()));
            PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, new PlayerData(uuid, false));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData playerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        playerData.setLoginTimestamp(System.currentTimeMillis());
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

    /**
     * Load data for owners of loaded tameables
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityLoad(EntityAddToWorldEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Tameable tameable)) return;
        UUID ownerId = tameable.getOwnerUniqueId();
        if (ownerId == null) return;
        PreventStabby.getPlugin().getPlayerManager().getPlayerData(ownerId);
    }

}
