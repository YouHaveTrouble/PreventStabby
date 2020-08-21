package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.players.SmartCache;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class WolfTargettingPlayerListener implements Listener {
    /**
     * Stops wolves with owners targetting players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfTarget(org.bukkit.event.entity.EntityTargetEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();
            if (wolf.getOwner() != null) {
                if (event.getTarget() instanceof Player) {
                    boolean attackerPvPEnabled = (boolean) SmartCache.getPlayerData(wolf.getOwner().getUniqueId()).get("pvpenabled");
                    Player victim = (Player) event.getTarget();
                    boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                    if (!attackerPvPEnabled || !victimPvpEnabled) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
