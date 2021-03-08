package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
            UUID victim = event.getEntity().getUniqueId();
            Firework firework = (Firework) event.getDamager();
            if (firework.getShooter() instanceof Player) {
                UUID damager = ((Player) firework.getShooter()).getUniqueId();
                if (damager == victim) {
                    return;
                }

                if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }
    }


}
