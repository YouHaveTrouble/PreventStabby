package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WolfAttackPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getDamager();
            if (wolf.getOwner() != null && event.getEntity() instanceof Player) {
                Player victim = (Player) event.getEntity();
                boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
                if (!victimPvpEnabled) {
                    wolf.setAngry(false);
                    event.setCancelled(true);
                }
            }
        }
    }

}
