package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Instant;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Firework firework = (Firework) event.getDamager();
            if (firework.getShooter() instanceof Player) {
                Player damager = (Player) firework.getShooter();
                if (damager == victim) {
                    return;
                }

                SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

                if (Instant.now().getEpochSecond() < smartCache.getPlayerData(victim.getUniqueId()).getLoginTimestamp()) {
                    event.setCancelled(true);
                    return;
                }

                if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_attacker());
                    return;
                }
                if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_victim());
                    return;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
            }
        }
    }


}
