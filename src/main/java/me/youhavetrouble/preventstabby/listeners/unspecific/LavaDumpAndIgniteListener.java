package me.youhavetrouble.preventstabby.listeners.unspecific;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.util.BoundingBoxUtil;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
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


@PreventStabbyListener
public class LavaDumpAndIgniteListener implements Listener {

    /**
     * Prevents dumping lava and pufferfish bucket near players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLavaDump(org.bukkit.event.player.PlayerBucketEmptyEvent event) {

        ConfigCache config = PreventStabby.getPlugin().getConfigCache();

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
                        if (PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(victim, damager)) {
                            event.setCancelled(true);
                            return;
                        }

                        if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
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
                    if (PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(victim, damager)) {
                        event.setCancelled(true);
                        return;
                    }

                    if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
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

        ConfigCache config = PreventStabby.getPlugin().getConfigCache();

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

                if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
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

                if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }

    }
}
