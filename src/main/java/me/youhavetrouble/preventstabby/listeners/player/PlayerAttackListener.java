package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@PreventStabbyListener
public class PlayerAttackListener implements Listener {

    /**
     * Cancels basic attacks done to player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity victimEntity = event.getEntity();

        if (damagerEntity instanceof Player && victimEntity instanceof Player) {
            UUID damager = damagerEntity.getUniqueId();
            UUID victim = victimEntity.getUniqueId();

            if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        }
    }

}
