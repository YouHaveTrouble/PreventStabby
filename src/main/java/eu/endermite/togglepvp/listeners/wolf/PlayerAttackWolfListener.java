package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerAttackWolfListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackWolf(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();
            if (wolf.getOwner() != null) {
                Player damager = (Player) event.getDamager();

                if (damager.getUniqueId() == wolf.getOwner().getUniqueId()) {
                    return;
                }

                ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                boolean damagerPvpState = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());

                if (!damagerPvpState) {
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                    event.setCancelled(true);
                    return;
                }
                boolean victimPvpEnabled = SmartCache.getPlayerData(wolf.getOwner().getUniqueId()).isPvpEnabled();
                if (!victimPvpEnabled) {
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                    event.setCancelled(true);
                    return;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), wolf.getOwner().getUniqueId());
            }
        }
    }
}
