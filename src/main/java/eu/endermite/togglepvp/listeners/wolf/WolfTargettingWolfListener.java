package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.players.SmartCache;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class WolfTargettingWolfListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttackWolf(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Wolf && event.getEntity() instanceof Wolf) {
            Wolf damager = (Wolf) event.getDamager();
            Wolf victim = (Wolf) event.getEntity();
            if (damager.getOwner() != null && damager.getOwner() != null) {
                try {
                    boolean damagerPvpEnabled = SmartCache.getPlayerData(damager.getOwner().getUniqueId()).isPvpEnabled();
                    boolean victimPvpEnabled = SmartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();
                    if (!victimPvpEnabled || !damagerPvpEnabled) {
                        damager.setAngry(false);
                        event.setCancelled(true);
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }
        }
    }

}
