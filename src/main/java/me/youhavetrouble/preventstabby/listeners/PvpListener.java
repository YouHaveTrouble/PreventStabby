package me.youhavetrouble.preventstabby.listeners;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.data.DamageCheckResult;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Locale;

public class PvpListener implements Listener {

    private final PreventStabby plugin;

    public PvpListener(PreventStabby plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        DamageCheckResult result = plugin.getPlayerManager().canDamage(attacker, victim);
        if (!result.ableToDamage()) {
            event.setCancelled(true);
        }
        plugin.getPlayerManager().handleDamageCheck(result);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player thrower)) return;
        boolean harmful = false;
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (!PotionEffectType.Category.HARMFUL.equals(effect.getType().getEffectCategory())) continue;
            harmful = true;
            break;
        }
        if (!harmful) return;
        for (LivingEntity victim : event.getAffectedEntities()) {
            if (thrower == victim) continue;
            DamageCheckResult result = plugin.getPlayerManager().canDamage(thrower, victim);
            if (!result.ableToDamage()) {
                event.setIntensity(victim, 0);
            }
            plugin.getPlayerManager().handleDamageCheck(result);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPotionCloudEffectApply(AreaEffectCloudApplyEvent event) {
        if (!(event.getEntity().getSource() instanceof Player thrower)) return;
        boolean harmful = false;
        AreaEffectCloud cloud = event.getEntity();
        if (cloud.getBasePotionType() == null) return;
        PotionType potionType = cloud.getBasePotionType();
        for (PotionEffect effect : potionType.getPotionEffects()) {
            if (!PotionEffectType.Category.HARMFUL.equals(effect.getType().getEffectCategory())) continue;
            harmful = true;
            break;
        }
        if (!harmful) return;
        event.getAffectedEntities().removeIf(victim -> {
            if (thrower == victim) return false;
            boolean shouldRemoveFromAffected = false;
            DamageCheckResult result = plugin.getPlayerManager().canDamage(thrower, victim);
            if (!result.ableToDamage()) {
                shouldRemoveFromAffected = true;
            }
            plugin.getPlayerManager().handleDamageCheck(result);
            return shouldRemoveFromAffected;
        });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Item) return;
        if (plugin.getConfigCache().allow_fishing_rod_pull) return;
        Player attacker = event.getPlayer();
        Entity victim = event.getCaught();
        if (victim == null) return;
        if (attacker == victim) return;
        DamageCheckResult result = plugin.getPlayerManager().canDamage(attacker, victim);
        if (!result.ableToDamage()) {
            event.setCancelled(true);
        }
        plugin.getPlayerManager().handleDamageCheck(result);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCommandInCombat(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayerManager().getPlayer(player.getUniqueId()).isInCombat()) return;
        String message = event.getMessage().toLowerCase(Locale.ROOT);
        if (message.startsWith("/")) {
            message = message.substring(1);
        }
        message = message.split(" ")[0];
        if (!plugin.getConfigCache().getCombatBlockedCommands().contains(message)) return;

        event.setCancelled(true);
        player.sendMessage(plugin.getConfigCache().cant_do_that_during_combat);
    }

}
