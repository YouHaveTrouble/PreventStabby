package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

@PreventStabbyListener
public class PetTargettingPlayerListener implements Listener {
    /**
     * Stops pets with owners targetting players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetTargetPlayer(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Tameable)) return;
        Tameable entity = (Tameable) event.getEntity();
        if (!(entity.getOwner() instanceof Player)) return;
        if (!(event.getTarget() instanceof Player)) return;

        UUID damager = entity.getOwner().getUniqueId();
        UUID victim = event.getTarget().getUniqueId();

        if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true, false)) {
            if (event.getReason().equals(EntityTargetEvent.TargetReason.TEMPT)) {
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager, victim);
        }
        else
            event.setCancelled(true);

    }
}
