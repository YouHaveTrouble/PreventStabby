package me.youhavetrouble.preventstabby.listeners;

import me.youhavetrouble.preventstabby.data.Target;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

/**
 * This class is a listener that performs various operations related to tagging entities with player UUIDs.
 */
public class UtilListener implements Listener {

    /**
     * Tag TNT minecart with uuid of player who placed it
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPlacedTntMinecart(EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ExplosiveMinecart minecart)) return;
        if (event.getPlayer() == null) return;
        Target.assignPlayerSourceId(minecart, event.getPlayer().getUniqueId());
    }

    /**
     * Tag TNT minecart with uuid of player who last nudged it
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerNudgedTntMinecart(VehicleEntityCollisionEvent event) {
        if (!(event.getVehicle() instanceof ExplosiveMinecart minecart)) return;
        if (!(event.getEntity() instanceof Player player)) return;
        Target.assignPlayerSourceId(minecart, player.getUniqueId());
    }

    /**
     * Tag primed TNT with players uuid
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPrimedTnt(ExplosionPrimeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed tntPrimed)) return;
        if (tntPrimed.getSource() instanceof TNTPrimed primingTnt) {
            Target primingTntTarget = Target.getTarget(primingTnt);
            if (primingTntTarget == null) return;
            Target.assignPlayerSourceId(tntPrimed, primingTntTarget.playerUuid);
            return;
        }
        if (tntPrimed.getSource() instanceof Player primingPlayer) {
            Target.assignPlayerSourceId(tntPrimed, primingPlayer.getUniqueId());
            return;
        }
    }

    /**
     * Tags ender crystal with exploding players uuid
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitEnderCrystal(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof EnderCrystal enderCrystal)) return;
        if (event.getDamager() instanceof Player player) {
            Target.assignPlayerSourceId(enderCrystal, player.getUniqueId());
            return;
        }
        if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            Target.assignPlayerSourceId(enderCrystal, player.getUniqueId());
            return;
        }
    }

}
