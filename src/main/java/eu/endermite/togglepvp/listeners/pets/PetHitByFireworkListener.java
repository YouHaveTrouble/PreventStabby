package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PetHitByFireworkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireworkDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Tameable) {
            Tameable victim = (Tameable) event.getEntity();
            if (victim.getOwner() == null)
                return;
            Firework firework = (Firework) event.getDamager();
            if (!(firework.getShooter() instanceof Player))
                return;
            Player damager = (Player) firework.getShooter();
            if (victim.getOwner() == damager)
                return;
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_pets_attacker());
                return;
            }
            if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager.getUniqueId(), TogglePvp.getPlugin().getConfigCache().getCannot_attack_pets_victim());
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());

        }
    }
}
