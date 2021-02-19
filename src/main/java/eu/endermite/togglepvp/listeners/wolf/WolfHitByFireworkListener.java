package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class WolfHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Wolf) {
            Wolf victim = (Wolf) event.getEntity();
            if (victim.getOwner() == null)
                return;
            Firework firework = (Firework) event.getDamager();
            if (firework.getShooter() instanceof Player) {
                Player damager = (Player) firework.getShooter();
                if (victim.getOwner() == damager)
                    return;
                boolean damagerPvpEnabled = TogglePvp.getPlugin().getPlayerManager().getPlayerPvPState(victim.getOwner().getUniqueId());
                if (!damagerPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_pets_attacker());
                    return;
                }
                boolean victimPvpEnabled = TogglePvp.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_pets_victim());
                    return;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
            }
        }
    }
}
