package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WolfAttackPlayerListener implements Listener {

    /**
     * Wolves stop following player with pvp off after trying to hit them
     * This is to fix any inconsistancy with wolf behaviour
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getDamager();
            if (wolf.getOwner() != null && event.getEntity() instanceof Player) {

                //TODO check if offline wolf owner has pvp on or off and cancel this accordingly

                Player victim = (Player) event.getEntity();
                boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!victimPvpEnabled) {
                    wolf.setAngry(false);
                    event.setCancelled(true);
                }
            }
        }
    }

}
