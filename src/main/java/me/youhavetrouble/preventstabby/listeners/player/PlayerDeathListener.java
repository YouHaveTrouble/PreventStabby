package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(org.bukkit.event.entity.EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        PlayerData playerData = PreventStabby.getPlugin().getSmartCache().getPlayerData(player.getUniqueId());
        playerData.setCombattime(0);
        playerData.setLastCombatCheck(false);
        playerData.setInCombat(false);
    }

}
