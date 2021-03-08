package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.SmartCache;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PetTargettingPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttackWolf(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Tameable && event.getEntity() instanceof Tameable) {
            Tameable damager = (Tameable) event.getDamager();
            Tameable victim = (Tameable) event.getEntity();
            if (damager.getOwner() == null || victim.getOwner() == null)
                return;

            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            boolean damagerPvpEnabled = smartCache.getPlayerData(damager.getOwner().getUniqueId()).isPvpEnabled();
            boolean victimPvpEnabled = smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();

            if (!victimPvpEnabled || !damagerPvpEnabled) {
                if (damager instanceof Wolf) {
                    Wolf wolf = (Wolf) damager;
                    wolf.setAngry(false);
                }
                event.setCancelled(true);
            }
        }
    }

}
