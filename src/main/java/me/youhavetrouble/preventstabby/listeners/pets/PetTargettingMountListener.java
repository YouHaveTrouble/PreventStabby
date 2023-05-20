package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.PvpState;
import me.youhavetrouble.preventstabby.util.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class PetTargettingMountListener implements Listener {

    /**
     * TODO - this needs to pass canDamage() in the future, for now it just checks forced pvp state directly
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttackMount(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (PreventStabby.getPlugin().getPlayerManager().getForcedPvpState() == PvpState.ENABLED) return;
        if (!(event.getDamager() instanceof Tameable) || event.getEntity().getPassengers().isEmpty()) return;
        Tameable damager = (Tameable) event.getDamager();
        Entity victim = event.getEntity();
        if (damager.getOwner() == null) return;

        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        boolean damagerPvpEnabled = smartCache.getPlayerData(damager.getOwner().getUniqueId()).isPvpEnabled();
        boolean victimPvpEnabled = !Util.processMountAttack(damager.getOwner().getUniqueId(), victim);

        if (!victimPvpEnabled || !damagerPvpEnabled) {
            if (damager instanceof Wolf) {
                Wolf wolf = (Wolf) damager;
                wolf.setAngry(false);
            }
            event.setCancelled(true);
        }

    }

}
