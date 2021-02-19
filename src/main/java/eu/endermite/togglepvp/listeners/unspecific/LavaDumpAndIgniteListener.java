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
public class LavaDumpAndIgniteListener implements Listener {

    private ConfigCache config = TogglePvP.getPlugin().getConfigCache();

    /**
     * Prevents dumping lava and pufferfish bucket near players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLavaDump(org.bukkit.event.player.PlayerBucketEmptyEvent event) {

        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        if (event.getBucket().equals(Material.LAVA_BUCKET) || event.getBucket().equals(Material.PUFFERFISH_BUCKET)) {
            Location location = event.getBlockClicked().getLocation();
            Player damager = event.getPlayer();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
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
                    if (victim.getOwner() == null) {
                        return;
                    }
                    boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                    if (!damagerPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    try {
                        boolean victimPvpEnabled = SmartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();
                        if (!victimPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                            event.setCancelled(true);
                            return;
                        }
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
                    } catch (NullPointerException ignored) {}
                }
            }
        }
    }

    /**
     * Prevents setting blocks on fire near players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onIgnite(org.bukkit.event.block.BlockIgniteEvent event) {

        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        if (event.getPlayer() !=null) {
            Location location = event.getBlock().getLocation();
            Player damager = event.getPlayer();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
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
                    if (victim.getOwner() == null || victim.getOwner() == damager) {
                        return;
                    }
                    boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                    if (!damagerPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    try {
                        boolean victimPvpEnabled = SmartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();
                        if (!victimPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                            event.setCancelled(true);
                            return;
                        }
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
                    } catch (NullPointerException ignored) {}
                }
            }
        }
    }
}
