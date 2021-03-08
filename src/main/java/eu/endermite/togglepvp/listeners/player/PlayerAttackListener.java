package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerAttackListener implements Listener {

    /**
     * Cancels basic attacks done to player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity victimEntity = event.getEntity();

        if (damagerEntity instanceof Player && victimEntity instanceof Player) {
            UUID damager = damagerEntity.getUniqueId();
            UUID victim = victimEntity.getUniqueId();

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        }
    }

}
