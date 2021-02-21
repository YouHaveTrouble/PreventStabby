package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PetLeashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetLeash(org.bukkit.event.entity.PlayerLeashEntityEvent event) {
        if (event.getEntity() instanceof Tameable) {
            Tameable victim = (Tameable) event.getEntity();
            if (victim.getOwner() == null)
                return;

            Player damager = event.getPlayer();
            if (victim.getOwner() == damager)
                return;

            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            if (!smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
            }
        }
    }

}
