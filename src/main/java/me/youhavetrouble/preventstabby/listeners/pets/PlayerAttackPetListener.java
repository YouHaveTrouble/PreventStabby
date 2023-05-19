package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

@PreventStabbyListener
public class PlayerAttackPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackPet(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Tameable)) return;

        Tameable tameable = (Tameable) event.getEntity();
        if (tameable.getOwner() == null) return;

        UUID damager = event.getDamager().getUniqueId();
        UUID victim = tameable.getOwner().getUniqueId();

        if (damager.equals(victim)) return;

        ConfigCache config = PreventStabby.getPlugin().getConfigCache();

        if (PreventStabby.getPlugin().getPlayerManager()
                .canDamage(
                        damager,
                        victim,
                        config.getCannot_attack_pets_attacker(),
                        config.getCannot_attack_pets_victim(),
                        false
                ))
            CombatTimer.refreshPlayersCombatTime(damager);
        else
            event.setCancelled(true);

    }
}
