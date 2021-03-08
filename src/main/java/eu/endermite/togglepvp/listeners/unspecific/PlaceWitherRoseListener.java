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
public class PlaceWitherRoseListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerWitherRosePlace(org.bukkit.event.block.BlockPlaceEvent event) {

        ConfigCache config = TogglePvp.getPlugin().getConfigCache();

        if (!config.isLava_and_fire_stopper_enabled())
            return;

        if (event.getBlock().getType().equals(Material.WITHER_ROSE)) {
            Location location = event.getBlockPlaced().getLocation();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    UUID damager = event.getPlayer().getUniqueId();
                    UUID victim = entity.getUniqueId();
                    if (victim == damager)
                        return;

                    if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                        CombatTimer.refreshPlayersCombatTime(damager, victim);
                    else
                        event.setCancelled(true);
                } else if (entity instanceof Tameable) {
                    Tameable tameable = (Tameable) entity;
                    UUID damager = event.getPlayer().getUniqueId();
                    if (tameable.getOwner() == null)
                        return;

                    UUID victim = tameable.getOwner().getUniqueId();
                    if (victim == damager)
                        return;

                    if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                        CombatTimer.refreshPlayersCombatTime(damager, victim);
                    else
                        event.setCancelled(true);
                }
            }
        }
    }
}
