package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FishingListener implements Listener {

    /**
     * Prevents hooking players with disabled pvp with fishing rod
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFish(org.bukkit.event.player.PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            Player damager = event.getPlayer();
            Player victim = (Player) event.getCaught();

            if (damager == victim) {
                return;
            }

            ConfigCache config = TogglePvP.getPlugin().getConfigCache();
            boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager);
            boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
            if (!damagerPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            if (!victimPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
            }
        }
    }

}
