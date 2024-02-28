package me.youhavetrouble.preventstabby.listeners;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.data.DamageCheckResult;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    private final PreventStabby plugin;

    public PlayerDamageListener(PreventStabby plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        DamageCheckResult result = plugin.getPlayerManager().canDamage(attacker, victim);

        if (!result.ableToDamage()) {
            event.setCancelled(true);
        }
        plugin.getPlayerManager().handleDamageCheck(result);

    }

}
