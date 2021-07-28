package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@PreventStabbyListener
public class PlayerHitByProjectileListener implements Listener {

    /**
     * Cancels damage done by projectiles to player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                UUID damager = ((Player) projectile.getShooter()).getUniqueId();
                UUID victim = event.getEntity().getUniqueId();

                if (PreventStabby.getPlugin().getConfigCache().isSnowballs_knockback() && event.getDamager() instanceof Snowball) {
                    if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true)) {
                        ((Player) event.getEntity()).damage(0.01, (Entity) projectile.getShooter());
                        CombatTimer.refreshPlayersCombatTime(damager, victim);
                    } else {
                        event.setCancelled(true);
                        return;
                    }

                } else if (PreventStabby.getPlugin().getConfigCache().isEgg_knockback() && event.getDamager() instanceof Egg) {
                    if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true)) {
                        ((Player) event.getEntity()).damage(0.01, (Entity) projectile.getShooter());
                        CombatTimer.refreshPlayersCombatTime(damager, victim);
                    } else {
                        event.setCancelled(true);
                        return;
                    }
                }

                // Ender pearls and other self-damage
                if (damager.equals(victim)) return;

                if (event.getDamage() == 0) return;

                if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }
    }

}
