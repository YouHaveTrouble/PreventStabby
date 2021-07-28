package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.UUID;

@PreventStabbyListener
public class PlayerHitByExplosionListener implements Listener {

    /**
     * Cancels explosion damage for players with pvp off that is caused by players
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;

            UUID victim = event.getEntity().getUniqueId();
            try {
                UUID damager = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
                if (victim.equals(damager)) return;

                if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);

            } catch (NullPointerException | IndexOutOfBoundsException ignored) { }
        }
    }

    /**
     * Tags ender crystal with exploder uuid
     */

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitEnderCrystal(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderCrystal) {
            EnderCrystal enderCrystal = (EnderCrystal) event.getEntity();
            if (event.getDamager() instanceof Player) {
                enderCrystal.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), event.getDamager().getUniqueId().toString()));
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player damager = (Player) projectile.getShooter();
                    enderCrystal.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), damager.getUniqueId().toString()));
                }
            }
        }
    }

    /**
     * Tag ender crystal with destroying players uuid
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPrimedTnt(org.bukkit.event.entity.ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tntPrimed = (TNTPrimed) event.getEntity();
            if (tntPrimed.getSource() instanceof Player) {
                Player damager = (Player) tntPrimed.getSource();
                tntPrimed.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), damager.getUniqueId().toString()));
            } else if (tntPrimed.getSource() instanceof TNTPrimed) {
                try {
                    TNTPrimed sourceTnt = (TNTPrimed) tntPrimed.getSource();
                    String damagerUuid = sourceTnt.getMetadata("PLAYEREXPLODED").get(0).asString();
                    tntPrimed.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), damagerUuid));
                } catch (NullPointerException ignored) {
                }
            }
        }
    }

    /**
     * Tag TNT minecart with placing player uuid
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPlacedTntMinecart(org.bukkit.event.entity.EntityPlaceEvent event) {
        if (event.getEntityType().equals(EntityType.MINECART_TNT)) {
            if (event.getPlayer() != null) {
                event.getEntity().setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), event.getPlayer().getUniqueId().toString()));
            }
        }
    }

    /**
     * Tag TNT minecart with uuid of player who last nudged it
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerNudgedTntMinecart(org.bukkit.event.vehicle.VehicleEntityCollisionEvent event) {
        if (event.getVehicle() instanceof ExplosiveMinecart && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getEntity();
            event.getVehicle().setMetadata("PLAYEREXPLODED", new FixedMetadataValue(PreventStabby.getPlugin(), damager.getUniqueId().toString()));
        }
    }

}
