package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.BoundingBoxUtil;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

@eu.endermite.togglepvp.util.Listener
public class PlaceWitherRoseListener implements Listener {

    private ConfigCache config = TogglePvP.getPlugin().getConfigCache();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerWitherRosePlace(org.bukkit.event.block.BlockPlaceEvent event) {
        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        if(event.getBlock().getType().equals(Material.WITHER_ROSE)) {
            Location location = event.getBlockPlaced().getLocation();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    Player damager = event.getPlayer();
                    Player victim = (Player) entity;
                    if (victim != damager) {
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
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
                    }
                } else if (entity instanceof Wolf) {
                    Wolf victim = (Wolf) entity;
                    Player damager = event.getPlayer();
                    if (victim.getOwner() == null || victim.getOwner() == damager) {
                        return;
                    }
                    boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                    if (!damagerPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    boolean victimPvpEnabled = SmartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();
                    if (!victimPvpEnabled) {
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
