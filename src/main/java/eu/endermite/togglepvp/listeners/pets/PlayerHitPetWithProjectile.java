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

import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitPetWithProjectile implements Listener {

     /**
     * Cancels damage done by projectiles to pets of players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitPetWithProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player))
                return;

            Tameable tameable = (Tameable) event.getEntity();

            if (tameable.getOwner() == null)
                return;

            UUID damager = ((Player) projectile.getShooter()).getUniqueId();
            UUID victim = tameable.getOwner().getUniqueId();

            if (damager == victim)
                return;

            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

            if (!smartCache.getPlayerData(damager).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager);

        }
    }
}
