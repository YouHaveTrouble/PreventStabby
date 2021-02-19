package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class WolfAttackPlayerListener implements Listener {

    /**
     * Wolves stop following player with pvp off after trying to hit them
     * This is to fix any inconsistancy with wolf behaviour
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getDamager();
            if (wolf.getOwner() != null && event.getEntity() instanceof Player) {
                try {
                    boolean damagerPvpEnabled = SmartCache.getPlayerData(wolf.getOwner().getUniqueId()).isPvpEnabled();
                    Player victim = (Player) event.getEntity();
                    boolean victimPvpEnabled = TogglePvp.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                    if (!victimPvpEnabled || !damagerPvpEnabled) {
                        wolf.setAngry(false);
                        event.setCancelled(true);
                        return;
                    }
                    CombatTimer.refreshPlayersCombatTime(wolf.getOwner().getUniqueId(), victim.getUniqueId());
                } catch (NullPointerException ignored) {}
            }
        }
    }

}
