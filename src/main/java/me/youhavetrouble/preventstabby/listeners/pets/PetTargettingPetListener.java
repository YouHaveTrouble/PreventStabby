package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class PetTargettingPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttackWolf(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Tameable && event.getEntity() instanceof Tameable) {
            Tameable damager = (Tameable) event.getDamager();
            Tameable victim = (Tameable) event.getEntity();
            if (damager.getOwner() == null || victim.getOwner() == null) return;

            SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
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
