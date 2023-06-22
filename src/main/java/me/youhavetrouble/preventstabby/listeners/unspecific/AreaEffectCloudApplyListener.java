package me.youhavetrouble.preventstabby.listeners.unspecific;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.UUID;

@PreventStabbyListener
public class AreaEffectCloudApplyListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCloudEffects(AreaEffectCloudApplyEvent event) {

        if (!(event.getEntity().getSource() instanceof Player)) return;
        PotionEffectType potionEffectType = event.getEntity().getBasePotionData().getType().getEffectType();
        if (potionEffectType == null) return;

        if (!Util.isPotionEffectHarmful(potionEffectType)) return;

        PlayerManager playerManager = PreventStabby.getPlugin().getPlayerManager();

        Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
        UUID damager = ((Player) event.getEntity().getSource()).getUniqueId();
        while (it.hasNext()) {
            LivingEntity entity = it.next();
            if (entity instanceof Player) {
                UUID victim = entity.getUniqueId();
                if (damager == victim) continue;

                if (playerManager.canDamage(damager, victim, true))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    it.remove();

            } else if (entity instanceof Tameable) {
                Tameable tameable = (Tameable) entity;

                if (tameable.getOwner() == null) continue;

                UUID victim = tameable.getOwner().getUniqueId();
                if (victim == damager) continue;

                if (playerManager.canDamage(damager, victim, true, false))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    it.remove();
            }
        }
    }
}
