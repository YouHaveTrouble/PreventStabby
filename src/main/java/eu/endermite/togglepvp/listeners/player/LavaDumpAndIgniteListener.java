package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

public class LavaDumpAndIgniteListener implements Listener {

    private ConfigCache config = TogglePvP.getPlugin().getConfigCache();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLavaDump(org.bukkit.event.player.PlayerBucketEmptyEvent event) {

        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        Location location = event.getBlockClicked().getLocation();
        Player damager = event.getPlayer();
        double radius = config.getLava_and_fire_stopper_radius();
        BoundingBox boundingBox = getBoundingBox(location, radius);

        for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
            if (entity instanceof Player) {
                Player victim = (Player) entity;
                if (victim != damager) {
                    boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager);
                    if (!damagerPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                        event.setCancelled(true);
                        return;
                    }
                    boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
                    if (!victimPvpEnabled) {
                        PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onIgnite(org.bukkit.event.block.BlockIgniteEvent event) {

        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        if (event.getPlayer() !=null) {
            Location location = event.getBlock().getLocation();
            Player damager = event.getPlayer();
            double radius = config.getLava_and_fire_stopper_radius();
            BoundingBox boundingBox = getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    Player victim = (Player) entity;
                    if (victim != damager) {
                        boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager);
                        if (!damagerPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                            event.setCancelled(true);
                            return;
                        }
                        boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
                        if (!victimPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }


    }


    private BoundingBox getBoundingBox(Location location, double radius) {

        double x1 = location.getX()+radius;
        double y1 = location.getY()+radius;
        double z1 = location.getZ()+radius;

        double x2 = location.getX()-radius;
        double y2 = location.getY()-radius;
        double z2 = location.getZ()-radius;

        return new BoundingBox(x1, y1, z1, x2, y2, z2);

    }

}
