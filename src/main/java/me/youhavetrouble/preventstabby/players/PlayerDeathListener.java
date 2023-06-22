package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@PreventStabbyListener
public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        PlayerData playerData = PreventStabby.getPlugin().getSmartCache().getPlayerData(player.getUniqueId());
        if (playerData == null) return;
        playerData.setCombattime(0);
        playerData.setLastCombatCheck(false);
        playerData.setInCombat(false);
    }

}
