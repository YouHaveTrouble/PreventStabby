package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetHitByExplosionListener implements Listener {

    /**
     * Cancels explosion damage for pets with pvp off that is caused by players
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Tameable))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        Tameable tameable = (Tameable) event.getEntity();
        if (tameable.getOwner() == null)
            return;

        UUID victim = tameable.getOwner().getUniqueId();

        try {
            UUID damager = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
            if (victim == damager)
                return;

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {}

    }
}
