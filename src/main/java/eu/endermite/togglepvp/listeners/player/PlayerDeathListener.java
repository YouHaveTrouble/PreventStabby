package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(org.bukkit.event.entity.EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        PlayerData playerData = TogglePvp.getPlugin().getSmartCache().getPlayerData(player.getUniqueId());
        playerData.setCombattime(0);
        playerData.setLastCombatCheck(false);
        playerData.setInCombat(false);
    }

}
