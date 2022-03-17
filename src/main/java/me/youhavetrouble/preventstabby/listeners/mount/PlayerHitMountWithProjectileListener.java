package me.youhavetrouble.preventstabby.listeners.mount;

import me.youhavetrouble.preventstabby.util.Util;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@PreventStabbyListener
public class PlayerHitMountWithProjectileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitMountWithProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) return;
        Projectile projectile = (Projectile) event.getDamager();
        if (!(projectile.getShooter() instanceof Player) || event.getEntity().getPassengers().isEmpty()) return;
        Entity entity = event.getEntity();
        UUID damager = event.getDamager().getUniqueId();
        if (Util.processMountAttack(damager, entity))
            event.setCancelled(true);
    }

}
