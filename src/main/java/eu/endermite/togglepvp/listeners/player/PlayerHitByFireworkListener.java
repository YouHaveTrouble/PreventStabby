package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
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
                boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!damagerPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvP.getPlugin().getConfigCache().getCannot_attack_attacker());
                    return;
                }
                boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvP.getPlugin().getConfigCache().getCannot_attack_victim());
                }
            }
        }
    }


}
