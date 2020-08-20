package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;


public class PlayerHitByExplosionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitEnderCrystal(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderCrystal && event.getDamager() instanceof Player) {
            EnderCrystal enderCrystal = (EnderCrystal) event.getEntity();
            enderCrystal.setMetadata("PLAYEREXPLODED", new FixedMetadataValue(TogglePvP.getPlugin(), event.getDamager().getUniqueId().toString()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            try {
                UUID playeruuid = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
                Player damager = Bukkit.getPlayer(playeruuid);
                if (victim != damager) {
                    ConfigCache config = TogglePvP.getPlugin().getConfigCache();
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
            } catch (NullPointerException ignored) {}
        }
    }
}
