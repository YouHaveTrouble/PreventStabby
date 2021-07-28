package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.util.UUID;

@PreventStabbyListener
public class PlayerJoinAndLeaveListener implements Listener {
    /**
     * This event is here to get players saved options on join
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData playerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        long time = Instant.now().getEpochSecond();
        if (playerData == null) {
            PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, new PlayerData(false));
            Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
                PlayerData data = PreventStabby.getPlugin().getSqLite().getPlayerInfo(uuid);
                PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, data);
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
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> PreventStabby.getPlugin().getSqLite().updatePlayerInfo(player.getUniqueId(), PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId())));

        if (!PreventStabby.getPlugin().getConfigCache().isPunish_for_combat_logout()) return;

        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        PlayerData playerData = smartCache.getPlayerData(player.getUniqueId());

        if (!playerData.isInCombat()) return;

        player.setHealth(0);
        if (PreventStabby.getPlugin().getConfigCache().isPunish_for_combat_logout_announce())
            PluginMessages.broadcastMessage(player, PreventStabby.getPlugin().getConfigCache().getPunish_for_combat_logout_message());


    }
}
