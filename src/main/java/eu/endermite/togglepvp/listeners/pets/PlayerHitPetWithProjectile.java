package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitPetWithProjectile implements Listener {

     /**
     * Cancels damage done by projectiles to pets of players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitWolfWithProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player))
                return;

            Tameable victim = (Tameable) event.getEntity();
            Player damager = (Player) projectile.getShooter();
            if (victim.getOwner() == null || victim.getOwner() == damager)
                return;

            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                return;
            }
            if (!smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());

        }
    }
}
