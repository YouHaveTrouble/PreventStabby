package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetTargettingPlayerListener implements Listener {
    /**
     * Stops pets with owners targetting players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetTargetPlayer(org.bukkit.event.entity.EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Tameable))
            return;

        Tameable entity = (Tameable) event.getEntity();
        if (entity.getOwner() == null)
            return;

        if (event.getTarget() instanceof Player) {
            UUID damager = entity.getOwner().getUniqueId();
            UUID victim = event.getTarget().getUniqueId();

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        }
    }
}
