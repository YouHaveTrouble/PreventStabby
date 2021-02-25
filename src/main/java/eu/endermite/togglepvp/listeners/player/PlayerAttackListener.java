package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Instant;

@eu.endermite.togglepvp.util.Listener
public class PlayerAttackListener implements Listener {

    /**
     * Cancels basic attacks done to player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity victimEntity = event.getEntity();

        if (damagerEntity instanceof Player && victimEntity instanceof Player) {
            Player damager = (Player) damagerEntity;
            Player victim = (Player) victimEntity;

            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

            if (Instant.now().getEpochSecond() < smartCache.getPlayerData(victim.getUniqueId()).getLoginTimestamp()) {
                event.setCancelled(true);
                return;
            }

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
        }
    }

}
