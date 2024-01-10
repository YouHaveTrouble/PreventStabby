package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class PetTargettingPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttackWolf(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Tameable) || !(event.getEntity() instanceof Tameable)) return;
        Tameable damager = (Tameable) event.getDamager();
        Tameable victim = (Tameable) event.getEntity();
        if (damager.getOwner() == null || victim.getOwner() == null) return;

        if (!PreventStabby.getPlugin().getPlayerManager()
                .canDamage(
                        damager.getUniqueId(),
                        victim.getUniqueId(),
                        false,
                        false
                ))
            event.setCancelled(true);


    }

}
