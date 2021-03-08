package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerAttackPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackPet(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Tameable) {
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.getOwner() == null)
                return;

            UUID damager = event.getDamager().getUniqueId();
            UUID victim = tameable.getOwner().getUniqueId();

            if (damager == victim)
                return;

            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            boolean damagerPvpState = TogglePvp.getPlugin().getPlayerManager().getPlayerPvPState(damager);
            if (!damagerPvpState) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager);

        }
    }
}
