package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PetAttackPlayerListener implements Listener {

    /**
     * Pets stop following player with pvp off after trying to hit them
     * This is to fix any inconsistancy with pet behavior
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Tameable))
            return;

        Tameable entity = (Tameable) event.getDamager();
        if (entity.getOwner() != null && event.getEntity() instanceof Player) {

            boolean damagerPvpEnabled = TogglePvp.getPlugin().getSmartCache().getPlayerData(entity.getOwner().getUniqueId()).isPvpEnabled();
            Player victim = (Player) event.getEntity();
            boolean victimPvpEnabled = TogglePvp.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
            if (!victimPvpEnabled || !damagerPvpEnabled) {
                if (entity instanceof Wolf) {
                    Wolf wolf = (Wolf) entity;
                    wolf.setAngry(false);
                }
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(entity.getOwner().getUniqueId(), victim.getUniqueId());
        }

    }

}
