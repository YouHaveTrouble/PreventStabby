package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class WolfHitByExplosionListener implements Listener {

    /**
     * Cancels explosion damage for wolves with pvp off that is caused by players
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wolf) {
            if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                return;
            }
            Wolf victim = (Wolf) event.getEntity();
            if (victim.getOwner() == null) {
                return;
            }
            try {
                UUID damageruuid = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
                if (victim.getOwner().getUniqueId() == damageruuid) {
                    return;
                }
                ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                boolean damagerPvpEnabled = (boolean) SmartCache.getPlayerData(damageruuid).get("pvpenabled");
                if (!damagerPvpEnabled) {
                    PluginMessages.sendActionBar(damageruuid, config.getCannot_attack_pets_attacker());
                    event.setCancelled(true);
                    return;
                }
                boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
                if (!victimPvpEnabled) {
                    PluginMessages.sendActionBar(damageruuid, config.getCannot_attack_pets_victim());
                    event.setCancelled(true);
                    return;
                }
                CombatTimer.refreshPlayersCombatTime(damageruuid, victim.getOwner().getUniqueId());
            } catch (NullPointerException | IndexOutOfBoundsException ignored) {}
        }
    }
}
