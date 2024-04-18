package me.youhavetrouble.preventstabby.listeners;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.data.DamageCheckResult;
import me.youhavetrouble.preventstabby.data.Target;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class EnvironmentalListener implements Listener {

    private final PreventStabby plugin;
    private final Set<Material> dangerousBuckets = new HashSet<>();

    public EnvironmentalListener(PreventStabby plugin) {
        this.plugin = plugin;
        dangerousBuckets.add(Material.LAVA_BUCKET);
        dangerousBuckets.add(Material.PUFFERFISH_BUCKET);
        dangerousBuckets.add(Material.POWDER_SNOW_BUCKET);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDangerousBucketDump(PlayerBucketEmptyEvent event) {
        ConfigCache config = plugin.getConfigCache();
        if (!config.bucket_stopper_enabled) return;
        if (!dangerousBuckets.contains(event.getBucket())) return;
        Location location = event.getBlockClicked().getLocation();
        Player placer = event.getPlayer();
        double radius = config.bucket_stopper_radius;

        BoundingBox boundingBox = BoundingBox.of(location.toVector(), radius, radius, radius);
        for (Entity victim : location.getWorld().getNearbyEntities(boundingBox)) {
            if (victim.getUniqueId() == placer.getUniqueId()) continue;
            Target victimTarget = Target.getTarget(victim);
            if (victimTarget == null) continue;
            DamageCheckResult result = plugin.getPlayerManager().canDamage(placer.getUniqueId(), victim.getUniqueId(), victimTarget.classifier);
            if (!result.ableToDamage()) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        ConfigCache config = plugin.getConfigCache();
        if (!config.fire_stopper_enabled) return;
        Entity igniter = event.getIgnitingEntity();
        if (igniter == null) return;
        Target igniterTarget = Target.getTarget(igniter);
        if (igniterTarget == null) return;
        Location location = event.getBlock().getLocation();
        double radius = config.fire_stopper_radius;

        BoundingBox boundingBox = BoundingBox.of(location.toVector(), radius, radius, radius);
        for (Entity victim : location.getWorld().getNearbyEntities(boundingBox)) {
            if (victim.getUniqueId() == igniterTarget.playerUuid) continue;
            Target victimTarget = Target.getTarget(victim);
            if (victimTarget == null) continue;
            DamageCheckResult result = plugin.getPlayerManager().canDamage(igniterTarget.playerUuid, victim.getUniqueId(), victimTarget.classifier);
            if (!result.ableToDamage()) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockIgnite(BlockPlaceEvent event) {
        ConfigCache config = plugin.getConfigCache();
        if (!config.block_stopper_enabled) return;
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation().toCenterLocation();
        Target target = Target.getTarget(player);
        if (target == null) return;
        double radius = config.block_stopper_radius;

        BoundingBox boundingBox = BoundingBox.of(location.toVector(), radius, radius, radius);
        for (Entity victim : location.getWorld().getNearbyEntities(boundingBox)) {
            if (victim.getUniqueId() == target.playerUuid) continue;
            Target victimTarget = Target.getTarget(victim);
            if (victimTarget == null) continue;
            DamageCheckResult result = plugin.getPlayerManager().canDamage(target.playerUuid, victim.getUniqueId(), victimTarget.classifier);
            if (!result.ableToDamage()) {
                event.setCancelled(true);
                break;
            }
        }
    }

}
