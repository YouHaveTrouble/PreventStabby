package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetLeashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetLeash(org.bukkit.event.entity.PlayerLeashEntityEvent event) {
        if (event.getEntity() instanceof Tameable) {
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.getOwner() == null)
                return;

            UUID damager = event.getPlayer().getUniqueId();
            UUID victim = tameable.getOwner().getUniqueId();
            if (victim == damager)
                return;

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        }
    }

}
