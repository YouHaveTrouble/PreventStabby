package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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

            ConfigCache config = TogglePvP.getPlugin().getConfigCache();

            boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
            boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());

            if (!damagerPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            if (!victimPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
        }
    }

}
