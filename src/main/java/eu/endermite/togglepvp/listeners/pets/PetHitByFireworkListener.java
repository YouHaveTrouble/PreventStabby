package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Tameable) {
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.getOwner() == null)
                return;
            Firework firework = (Firework) event.getDamager();
            if (!(firework.getShooter() instanceof Player))
                return;
            UUID damager = ((Player) firework.getShooter()).getUniqueId();
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
