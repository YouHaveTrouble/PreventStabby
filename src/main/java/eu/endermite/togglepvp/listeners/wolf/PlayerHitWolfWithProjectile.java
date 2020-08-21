package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitWolfWithProjectile implements Listener {

    /**
     * Cancels damage done by projectiles to pets of players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitWolfWithProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wolf && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Wolf victim = (Wolf) event.getEntity();
                if (victim.getOwner() == null) {
                    return;
                }
                Player damager = (Player) projectile.getShooter();
                ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
                if (!damagerPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                    return;
                }
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                    return;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
            }
        }
    }
}
