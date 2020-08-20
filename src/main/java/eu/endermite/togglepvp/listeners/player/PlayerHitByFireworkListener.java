package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Firework firework = (Firework) event.getDamager();
            if (firework.getShooter() instanceof Player) {
                Player damager = (Player) firework.getShooter();
                if (damager == victim) {
                    return;
                }
                boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                }

            }

        }
    }


}
