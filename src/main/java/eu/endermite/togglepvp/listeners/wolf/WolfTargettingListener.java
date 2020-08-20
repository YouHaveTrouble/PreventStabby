package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WolfTargettingListener implements Listener {

    /**
     * Stops wolves with owners targetting players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfTarget(org.bukkit.event.entity.EntityTargetEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();
            if (wolf.getOwner() != null) {
                if (event.getTarget() instanceof Player) {
                    Player victim = (Player) event.getTarget();
                    boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                    if (!victimPvpEnabled) {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (event.getEntity() instanceof Fox) {
            Fox fox = (Fox) event.getEntity();
            if (fox.getFirstTrustedPlayer() != null) {
                if (event.getTarget() instanceof Player) {
                    Player victim = (Player) event.getTarget();
                    boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                    if (!victimPvpEnabled) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
