package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;


public class PlayerHitByExplosionListener implements Listener {


    /**
     * Cancels explosion damage for players with pvp off that is caused by players
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {

            if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                return;
            }

            Player victim = (Player) event.getEntity();
            try {
                UUID playeruuid = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
                Player damager = Bukkit.getPlayer(playeruuid);
                if (victim != damager) {
                    ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                    boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                    if (!damagerPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                    if (!victimPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                        event.setCancelled(true);
                        return;
                    }
                }
            } catch (NullPointerException | IndexOutOfBoundsException ignored) {}
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
                enderCrystal.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(TogglePvP.getPlugin(), event.getDamager().getUniqueId().toString()));
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player damager = (Player) projectile.getShooter();
                    enderCrystal.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(TogglePvP.getPlugin(), damager.getUniqueId().toString()));
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
                tntPrimed.setMetadata("PLAYEREXPLODED",new FixedMetadataValue( TogglePvP.getPlugin(), damager.getUniqueId().toString()));
            } else if (tntPrimed.getSource() instanceof TNTPrimed) {
                try {
                    TNTPrimed sourceTnt = (TNTPrimed) tntPrimed.getSource();
                    String damagerUuid = sourceTnt.getMetadata("PLAYEREXPLODED").get(0).asString();
                    tntPrimed.setMetadata("PLAYEREXPLODED", new FixedMetadataValue( TogglePvP.getPlugin(), damagerUuid));
                }catch (NullPointerException ignored) {}
            }
        }
    }

    /**
     * Tag TNT minecart with placing player uuid
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPlacedTntMinecart(org.bukkit.event.entity.EntityPlaceEvent event) {
        if (event.getEntityType().equals(EntityType.MINECART_TNT)) {
            if (event.getPlayer() != null) {
                event.getEntity().setMetadata("PLAYEREXPLODED", new FixedMetadataValue( TogglePvP.getPlugin(), event.getPlayer().getUniqueId().toString()));
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
            event.getVehicle().setMetadata("PLAYEREXPLODED", new FixedMetadataValue( TogglePvP.getPlugin(), damager.getUniqueId().toString()));
        }
    }

}
