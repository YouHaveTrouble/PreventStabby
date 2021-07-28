package me.youhavetrouble.preventstabby.listeners.mount;

import me.youhavetrouble.preventstabby.util.Util;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

@PreventStabbyListener
public class MountHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackMount(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Firework) || event.getEntity().getPassengers().isEmpty()) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
        Firework firework = (Firework) event.getDamager();
        if (!(firework.getShooter() instanceof Player)) return;
        UUID damager = ((Player) firework.getShooter()).getUniqueId();
        Entity mount = event.getEntity();
        if (Util.processMountAttack(damager, mount))
            event.setCancelled(true);
    }

}
