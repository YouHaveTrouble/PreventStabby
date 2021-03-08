package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class FishingListener implements Listener {

    /**
     * Prevents hooking players with disabled pvp with fishing rod
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFish(org.bukkit.event.player.PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            UUID damager = event.getPlayer().getUniqueId();
            UUID victim = event.getCaught().getUniqueId();
            if (damager == victim)
                return;

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        } else if (event.getCaught() instanceof Tameable) {
            Tameable tameable = (Tameable) event.getCaught();
            UUID damager = event.getPlayer().getUniqueId();
            if (tameable.getOwner() == null)
                return;

            UUID victim = tameable.getOwner().getUniqueId();

            if (damager == victim)
                return;

            if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            else
                event.setCancelled(true);
        }
    }
}
