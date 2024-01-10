package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.DamageCheck;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.PvpState;
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
        if (!(event.getDamager() instanceof Tameable damager) || event.getEntity().getPassengers().isEmpty()) return;
        Entity victim = event.getEntity();
        if (damager.getOwner() == null) return;

        DamageCheck.DamageCheckResult result = PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim);

        if (!result.ableToDamage) {
            if (damager instanceof Wolf wolf) {
                wolf.setAngry(false);
            }
            event.setCancelled(true);
        }

    }

}
