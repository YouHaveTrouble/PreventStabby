package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.PlayerManager;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetAttackPlayerListener implements Listener {

    /**
     * Pets stop following player with pvp off after trying to hit them
     * This is to fix any inconsistancy with pet behavior
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Tameable))
            return;

        Tameable entity = (Tameable) event.getDamager();
        if (entity.getOwner() != null && event.getEntity() instanceof Player) {

            UUID victim = event.getEntity().getUniqueId();
            UUID damager = entity.getOwner().getUniqueId();

            PlayerManager playerManager = TogglePvp.getPlugin().getPlayerManager();

            if (!playerManager.canDamage(damager, victim, false)) {
                if (entity instanceof Wolf) {
                    Wolf wolf = (Wolf) entity;
                    wolf.setAngry(false);
                }
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager, victim);
        }

    }

}
