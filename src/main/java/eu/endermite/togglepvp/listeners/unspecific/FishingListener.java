package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class FishingListener implements Listener {

    ConfigCache config = TogglePvp.getPlugin().getConfigCache();

    /**
     * Prevents hooking players with disabled pvp with fishing rod
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFish(org.bukkit.event.player.PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            Player damager = event.getPlayer();
            Player victim = (Player) event.getCaught();
            if (damager == victim) {
                return;
            }
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
        } else if (event.getCaught() instanceof Tameable) {
            Tameable victim = (Tameable) event.getCaught();
            Player damager = event.getPlayer();
            if (victim.getOwner() == null || victim.getOwner() == damager) {
                return;
            }
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            if (!smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
        }
    }
}
