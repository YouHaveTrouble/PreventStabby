package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.BoundingBoxUtil;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.util.UUID;


@eu.endermite.togglepvp.util.Listener
public class LavaDumpAndIgniteListener implements Listener {

    /**
     * Prevents dumping lava and pufferfish bucket near players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLavaDump(org.bukkit.event.player.PlayerBucketEmptyEvent event) {

        ConfigCache config = TogglePvp.getPlugin().getConfigCache();

        if (!config.isLava_and_fire_stopper_enabled())
            return;

        if (event.getBucket().equals(Material.LAVA_BUCKET) || event.getBucket().equals(Material.PUFFERFISH_BUCKET)) {
            Location location = event.getBlockClicked().getLocation();
            UUID damager = event.getPlayer().getUniqueId();
            double radius = config.getLava_and_fire_stopper_radius();

            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    UUID victim = entity.getUniqueId();
                    if (victim != damager) {
                        if (TogglePvp.getPlugin().getPlayerManager().hasLoginProtection(victim, damager)) {
                            event.setCancelled(true);
                            return;
                        }

                        if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                            CombatTimer.refreshPlayersCombatTime(damager, victim);
                        else
                            event.setCancelled(true);

                    }
                } else if (entity instanceof Tameable) {
                    Tameable tameable = (Tameable) entity;
                    if (tameable.getOwner() == null) {
                        return;
                    }

                    UUID victim = tameable.getOwner().getUniqueId();
                    if (TogglePvp.getPlugin().getPlayerManager().hasLoginProtection(victim, damager)) {
                        event.setCancelled(true);
                        return;
                    }

                    if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                        CombatTimer.refreshPlayersCombatTime(damager, victim);
                    else
                        event.setCancelled(true);

                }
            }
        }
    }

    /**
     * Prevents setting blocks on fire near players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onIgnite(org.bukkit.event.block.BlockIgniteEvent event) {

        ConfigCache config = TogglePvp.getPlugin().getConfigCache();

        if (!config.isLava_and_fire_stopper_enabled())
            return;

        if (event.getPlayer() == null)
            return;

        Location location = event.getBlock().getLocation();
        UUID damager = event.getPlayer().getUniqueId();
        double radius = config.getLava_and_fire_stopper_radius();
        BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
        for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
            if (entity instanceof Player) {
                UUID victim = entity.getUniqueId();
                if (victim == damager)
                    continue;

                if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);

            } else if (entity instanceof Tameable) {
                Tameable tameable = (Tameable) entity;
                if (tameable.getOwner() == null) {
                    continue;
                }

                UUID victim = tameable.getOwner().getUniqueId();
                if (victim == damager)
                    continue;

                if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }

    }
}
