package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PetTargettingPlayerListener implements Listener {
    /**
     * Stops pets with owners targetting players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfTarget(org.bukkit.event.entity.EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Tameable))
            return;

        Tameable entity = (Tameable) event.getEntity();
        if (entity.getOwner() == null)
            return;

        if (event.getTarget() instanceof Player) {
            boolean attackerPvPEnabled = TogglePvp.getPlugin().getSmartCache().getPlayerData(entity.getOwner().getUniqueId()).isPvpEnabled();
            Player victim = (Player) event.getTarget();
            boolean victimPvpEnabled = TogglePvp.getPlugin().getSmartCache().getPlayerData(victim.getUniqueId()).isPvpEnabled();
            if (!attackerPvPEnabled || !victimPvpEnabled) {
                event.setCancelled(true);
            }
        }
    }
}
