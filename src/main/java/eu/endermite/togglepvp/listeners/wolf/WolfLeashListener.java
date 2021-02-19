package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class WolfLeashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfLeash(org.bukkit.event.entity.PlayerLeashEntityEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf victim = (Wolf) event.getEntity();
            if (victim.getOwner() == null) {
                return;
            }
            Player damager = event.getPlayer();
            if (victim.getOwner() == damager) {
                return;
            }
            ConfigCache config = TogglePvP.getPlugin().getConfigCache();
            boolean damagerPvpEnabled = SmartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled();
            if (!damagerPvpEnabled) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            boolean victimPvpEnabled = SmartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled();
            if (!victimPvpEnabled) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
            }
        }
    }

}
