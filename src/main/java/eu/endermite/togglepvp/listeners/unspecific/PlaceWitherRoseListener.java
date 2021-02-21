package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.BoundingBoxUtil;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

@eu.endermite.togglepvp.util.Listener
public class PlaceWitherRoseListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerWitherRosePlace(org.bukkit.event.block.BlockPlaceEvent event) {

        ConfigCache config = TogglePvp.getPlugin().getConfigCache();
        SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

        if (!config.isLava_and_fire_stopper_enabled())
            return;

        if (event.getBlock().getType().equals(Material.WITHER_ROSE)) {
            Location location = event.getBlockPlaced().getLocation();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    Player damager = event.getPlayer();
                    Player victim = (Player) entity;
                    if (victim == damager)
                        return;
                    if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                        event.setCancelled(true);
                        return;
                    }
                    CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
                } else if (entity instanceof Tameable) {
                    Tameable victim = (Tameable) entity;
                    Player damager = event.getPlayer();
                    if (victim.getOwner() == null || victim.getOwner() == damager) {
                        return;
                    }
                    if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    if (!smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                        event.setCancelled(true);
                        return;
                    }
                    CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
                }
            }
        }
    }
}
